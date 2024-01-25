package ru.vavtech.songbase.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import ru.vavtech.songbase.model.Song;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class ExcelParser {

    public static List<Song> parseExcelStream(InputStream inputStream) {
        List<Song> songs = new ArrayList<>();

        try (Workbook workbook = new HSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();

            // Пропустим заголовок
            if (iterator.hasNext()) {
                iterator.next();
            }

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();

                Song song = new Song();

                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();
                    int columnIndex = currentCell.getColumnIndex();

                    switch (columnIndex) {
                        case 0:
                            song.setCode(currentCell.getStringCellValue());
                            break;
                        case 1:
                            song.setTitle(currentCell.getStringCellValue());
                            break;
                        case 2:
                            song.setArtist(currentCell.getStringCellValue());
                            break;
                        case 3:
                            song.setBack(currentCell.getStringCellValue());
                            break;
                        case 4:
                            song.setType(currentCell.getStringCellValue());
                            break;
                    }
                }
                songs.add(song);
            }

        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        return songs;
    }
}
