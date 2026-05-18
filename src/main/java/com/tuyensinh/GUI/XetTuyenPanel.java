package com.tuyensinh.GUI;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.tuyensinh.BUS.DiemXetTuyenBUS;
import com.tuyensinh.DTO.DiemXetTuyenDTO;

public class XetTuyenPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private DiemXetTuyenBUS bus;

    public XetTuyenPanel() {

        bus = new DiemXetTuyenBUS();

        setLayout(new BorderLayout());

        String[] cols = {
                "CCCD", "Họ", "Tên", "NV",
                "Phương thức", "Điểm THXT",
                "Điểm cộng", "Điểm UT", "Điểm xét tuyển"
        };

        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {

        model.setRowCount(0);

        List<DiemXetTuyenDTO> list = bus.getAll();

        for (DiemXetTuyenDTO d : list) {

            model.addRow(new Object[]{
                    d.getCccd(),
                    d.getHo(),
                    d.getTen(),
                    d.getNvtt(),
                    d.getThm(),
                    d.getDiemThxt(),
                    d.getDiemCong(),
                    d.getDiemUtqd(),
                    d.getDiemXetTuyen()
            });
        }
    }
}