/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AddFunctionsDialog.java
 *
 * Created on 8 avr. 2011, 09:39:48
 */

package modbuspal.slave;

import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import modbuspal.instanciator.InstantiableManager;

/**
 * the dialog where the user selects a ModbusPduProcessor to associate with
 * a function code
 * @author nnovic
 */
public class AddFunctionsDialog
extends javax.swing.JDialog
{
    private final ListOfFunctions listOfFunctions;
    private final ListOfInstances listOfInstances;
    private boolean isOK = false;

    class ListOfInstances
    extends AbstractListModel
    implements ComboBoxModel
    {
        private final HashMap<String,ModbusPduProcessor> instances = new HashMap<String,ModbusPduProcessor>();
        private final String[] names;
        private String selectedInstance;

        ListOfInstances(ModbusPduProcessor tab[])
        {
            names = new String[tab.length];
            for(int i=0; i<tab.length; i++)
            {
                names[i] = InstantiableManager.makeInstanceName( tab[i] );
                instances.put(names[i],tab[i]);
            }
        }

        @Override
        public int getSize() {
            return names.length;
        }

        @Override
        public Object getElementAt(int index) {
            return names[index];
        }

        @Override
        public void setSelectedItem(Object anItem) {
            selectedInstance = (String)anItem;
        }

        @Override
        public Object getSelectedItem() {
            return selectedInstance;
        }

        public ModbusPduProcessor getSelectedInstance()
        {
            return instances.get( selectedInstance );
        }

    }

    class ListOfFunctions
    extends DefaultComboBoxModel
    {
        private final InstantiableManager<ModbusPduProcessor> ffactory;
        ListOfFunctions(InstantiableManager<ModbusPduProcessor> ff) {
            super(ff.getList());
            ffactory = ff;
        }
        ModbusPduProcessor createFunction()
        throws InstantiationException, IllegalAccessException
        {
            String sel = (String)getSelectedItem();
            return ffactory.newInstance(sel);
        }
    }

    /** Creates new form AddFunctionsDialog 
     * @param parent the parent frame
     * @param ff the library of instantiable ModbusPduProcessors
     * @param slave the modbus slave into which the function will be added
     */
    public AddFunctionsDialog(Frame parent, InstantiableManager<ModbusPduProcessor> ff, ModbusSlave slave)
    {
        super(parent, true);
        listOfFunctions = new ListOfFunctions(ff);
        listOfInstances = new ListOfInstances(slave.getPduProcessorInstances());
        initComponents();
        Image img = Toolkit.getDefaultToolkit().createImage( getClass().getResource("/modbuspal/main/img/icon32.png") );
        setIconImage(img);
        if( listOfFunctions.getSize()>0 )
        {
            newRadioButton.requestFocus();
        }
        else
        {
            newRadioButton.setEnabled(false);
            newComboBox.setEnabled(false);
            existingRadioButton.requestFocus();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        instanceGroup = new javax.swing.ButtonGroup();
        existingRadioButton = new javax.swing.JRadioButton();
        newRadioButton = new javax.swing.JRadioButton();
        existingComboBox = new javax.swing.JComboBox();
        newComboBox = new javax.swing.JComboBox();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Select a PDU processor");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        instanceGroup.add(existingRadioButton);
        existingRadioButton.setText("Existing instance:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        getContentPane().add(existingRadioButton, gridBagConstraints);

        instanceGroup.add(newRadioButton);
        newRadioButton.setSelected(true);
        newRadioButton.setText("New instance:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        getContentPane().add(newRadioButton, gridBagConstraints);

        existingComboBox.setModel(listOfInstances);
        existingComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                existingComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(existingComboBox, gridBagConstraints);

        newComboBox.setModel(listOfFunctions);
        newComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(newComboBox, gridBagConstraints);

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 5);
        getContentPane().add(okButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 5);
        getContentPane().add(cancelButton, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        isOK = false;
        setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        isOK=true;
        setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed

    private void newComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newComboBoxActionPerformed
        newRadioButton.setSelected(true);
    }//GEN-LAST:event_newComboBoxActionPerformed

    private void existingComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_existingComboBoxActionPerformed
        existingRadioButton.setSelected(true);
    }//GEN-LAST:event_existingComboBoxActionPerformed

    boolean isNewInstance()
    {
        return newRadioButton.isSelected();
    }

    boolean isExistingInstance()
    {
        return existingRadioButton.isSelected();
    }

    ModbusPduProcessor getInstance()
    {
        if( isOK == false )
        {
            return null;
        }
        
        if( isNewInstance() )
        {
            try {
                return listOfFunctions.createFunction();
            } catch (InstantiationException ex) {
                Logger.getLogger(AddFunctionsDialog.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            } catch (IllegalAccessException ex) {
                Logger.getLogger(AddFunctionsDialog.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }

        }
        else if( isExistingInstance() )
        {
            return listOfInstances.getSelectedInstance();
        }
        return null;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox existingComboBox;
    private javax.swing.JRadioButton existingRadioButton;
    private javax.swing.ButtonGroup instanceGroup;
    private javax.swing.JComboBox newComboBox;
    private javax.swing.JRadioButton newRadioButton;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables

}
