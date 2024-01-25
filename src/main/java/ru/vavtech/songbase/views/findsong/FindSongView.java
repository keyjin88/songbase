package ru.vavtech.songbase.views.findsong;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.apache.commons.lang3.StringUtils;
import ru.vavtech.songbase.model.Song;
import ru.vavtech.songbase.repositories.SongRepository;
import ru.vavtech.songbase.views.MainLayout;

@PageTitle("Найти песню")
@Route(value = "find-song", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class FindSongView extends VerticalLayout {

    private final SongRepository repo;
    private final Grid<Song> grid;
    private TextField filter;
    private Button findButton;

    public FindSongView(SongRepository repo) {
        configureFilterAndButton();
        this.repo = repo;
        grid = new Grid<>(Song.class, false);
        grid.addColumn(Song::getCode).setHeader("Код");
        grid.addColumn(Song::getArtist).setHeader("Исполнитель");
        grid.addColumn(Song::getTitle).setHeader("Песня");
        grid.setSizeFull();

        add(filter, findButton, grid);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setSpacing(true);
    }

    private void configureFilterAndButton() {
        filter = new TextField();
        filter.setPlaceholder("Фильтр по названию или исполнителю");
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        findButton = new Button("Найти");
        findButton.addClickListener(event -> {
            String filterValue = filter.getValue();
            listSongs(filterValue);
        });
    }

    private void listSongs(String filterText) {
        if (StringUtils.isNotEmpty(filterText)) {
            grid.setItems(repo.findAllByFilter(filterText));
        } else {
            grid.setItems();
        }
    }

}
