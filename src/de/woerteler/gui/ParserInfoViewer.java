package de.woerteler.gui;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * Output frame for parser info messages.
 *
 * @author Leo Woerteler
 */
public class ParserInfoViewer extends JScrollPane {

  /** Serial version UID. */
  private static final long serialVersionUID = -6447515072417218139L;

  /**
   * Constructor taking the table model for the info table.
   *
   * @param model table model
   */
  ParserInfoViewer(final TableModel model) {
    final JTable table = new JTable(model);
    model.addTableModelListener(new TableModelListener() {

      @Override
      public void tableChanged(final TableModelEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            getVerticalScrollBar().setValue(
                getVerticalScrollBar().getMaximum());
          }
        });
      }
    });
    table.setFillsViewportHeight(true);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    setViewportView(table);
  }

}
