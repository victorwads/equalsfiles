/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing;

// <editor-fold defaultstate="collapsed" desc="Imports">
import br.com.victorwads.equalsfiles.swing.util.Dialogos;
import br.com.victorwads.equalsfiles.model.MD5Cache;
import br.com.victorwads.equalsfiles.swing.components.Janela;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Map;
import javax.swing.JFrame;

/**
 *
 * @author victo
 */
public class WindowFBDSave extends Janela {

	File diretorio;
	Thread running;
	JFrame parent;

	/**
	 * Creates new form ConvertBDFile
	 *
	 * @param file
	 * @param parent
	 */
	public WindowFBDSave(File file, JFrame parent) {
		initComponents();
		setVisible(true);

		this.parent = parent;
		this.parent.setVisible(false);
		this.diretorio = file;

		running = new Thread(processo);
		running.start();
	}

	// <editor-fold defaultstate="collapsed" desc="Methodos">
	private void loading(int i) {
		barProgresso.setValue(i);
		barProgresso.setString(new DecimalFormat("#.##").format((float) i / (float) barProgresso.getMaximum() * 100f) + " %");
	}

	private boolean stop = false;
	final Runnable processo = new Runnable() {
		@Override
		public void run() {
			MD5Cache m = br.com.victorwads.equalsfiles.controller.CacheMD5.listar();
			barProgresso.setMaximum(m.size());
			PrintWriter out;
			try {
				out = new PrintWriter(new BufferedWriter(new FileWriter(diretorio)));
			} catch (Exception e) {
				e.printStackTrace();
				Dialogos.erro("Falha ao criar o arquivo '" + diretorio + "cacheMD5.txt'.");
				return;
			}
			loading(0);
			for (Map.Entry<String, String> e : m.entrySet()) {
				if (stop) {
					break;
				}
				out.println(e.getValue() + e.getKey());
				loading(barProgresso.getValue() + 1);
			}
			out.close();
			loading(m.size());

			Dialogos.info("Arquivo salvo!");
			setVisible(false);
			close();
		}
	};

	private void close() {
		parent.setVisible(true);
		setVisible(false);
		dispose();
	}
	// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        barProgresso = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Salvando Arquivo");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(barProgresso, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(barProgresso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
	// <editor-fold defaultstate="collapsed" desc="Eventos">
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
		if (running.isAlive()) {
			stop = true;
		}
		close();
    }//GEN-LAST:event_formWindowClosing
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Variáveis">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barProgresso;
    // End of variables declaration//GEN-END:variables
	// </editor-fold>
}
