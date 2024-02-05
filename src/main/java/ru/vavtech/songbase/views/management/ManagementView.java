package ru.vavtech.songbase.views.management;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.vavtech.songbase.model.Song;
import ru.vavtech.songbase.repositories.SongRepository;
import ru.vavtech.songbase.utils.ExcelParser;
import ru.vavtech.songbase.views.MainLayout;

import java.io.InputStream;
import java.util.List;

@PageTitle("Управление")
@Route(value = "admin", layout = MainLayout.class)
public class ManagementView extends VerticalLayout {
    private Button deleteAllButton;
    private Button loginButton;
    private PasswordField passwordField;
    private VerticalLayout mainContent;

    // Захардкодим пароль
    private static final String CORRECT_PASSWORD = "pass";

    public ManagementView(SongRepository repository) {

        // Инициализация полей
        passwordField = new PasswordField("Введите пароль:");
        loginButton = new Button("Войти");
        mainContent = new VerticalLayout();

        // Настройка слушателя событий для кнопки
        loginButton.addClickListener(event -> {
            String enteredPassword = passwordField.getValue();

            // Проверка правильности пароля
            if (enteredPassword.equals(CORRECT_PASSWORD)) {
                // Если пароль верный, отображаем интерфейс
                remove(passwordField, loginButton);
                add(mainContent);

                deleteAllButton = new Button("Очистить БД");
                deleteAllButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
                deleteAllButton.addClickListener(event1 -> {
                    repository.deleteAll();
                });
                // Ваш существующий код для загрузки файла и обработки данных
                MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
                Upload upload = new Upload(buffer);

                upload.addSucceededListener(uploadEvent -> {
                    String fileName = uploadEvent.getFileName();
                    InputStream inputStream = buffer.getInputStream(fileName);
                    List<Song> songs = ExcelParser.parseExcelStream(inputStream);
                    repository.saveAll(songs);
                    System.out.println("Test");
                });

                mainContent.add(deleteAllButton, upload);
            } else {
                Notification.show("Неверный пароль", 3000, Notification.Position.MIDDLE);
            }
        });

        // Добавление компонентов на экран
        add(passwordField, loginButton);
    }
}
