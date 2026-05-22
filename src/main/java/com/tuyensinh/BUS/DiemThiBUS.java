package com.tuyensinh.BUS;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.tuyensinh.DAO.DiemThiDAO;
import com.tuyensinh.DTO.DiemThiDTO;

public class DiemThiBUS {

    private DiemThiDAO dao = new DiemThiDAO();
    public void importFromExcel(File file) {
        List<DiemThiDTO> list = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);
                Workbook wb = WorkbookFactory.create(fis)) {

            Sheet sheet = wb.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue;

                DiemThiDTO d = new DiemThiDTO();

                d.setCccd(getString(row.getCell(1)));

                d.setTo(getDouble(row.getCell(7)));
                d.setVa(getDouble(row.getCell(8)));
                d.setLi(getDouble(row.getCell(9)));
                d.setHo(getDouble(row.getCell(10)));
                d.setSi(getDouble(row.getCell(11)));
                d.setSu(getDouble(row.getCell(12)));
                d.setDi(getDouble(row.getCell(13)));

                d.setN1_thi(getDouble(row.getCell(15)));

                d.setKtpl(getDouble(row.getCell(17)));
                d.setTi(getDouble(row.getCell(18)));
                d.setCncn(getDouble(row.getCell(19)));
                d.setCnnn(getDouble(row.getCell(20)));

                d.setNk1(getDouble(row.getCell(22)));
                d.setNk2(getDouble(row.getCell(23)));

                list.add(d);
            }

            dao.insertBatch(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy tất cả điểm thi
     */
    public List<DiemThiDTO> getAll() {
        return dao.getAll();
    }

    /**
     * Lấy điểm thi theo CCCD
     */
    public DiemThiDTO getByCCCD(String cccd) {
        return dao.getByCCCD(cccd);
    }

    /**
     * Xóa điểm thi theo CCCD
     */
    public boolean delete(String cccd) {
        return dao.delete(cccd);
    }

    private String getString(Cell cell) {
        if (cell == null)
            return "";
        return cell.toString().trim();
    }

    private double getDouble(Cell cell) {
        if (cell == null)
            return 0;
        try {
            return cell.getNumericCellValue();
        } catch (Exception e) {
            return 0;
        }
    }
}
