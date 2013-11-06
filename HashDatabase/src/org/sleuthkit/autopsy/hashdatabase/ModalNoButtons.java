/*
 * Autopsy Forensic Browser
 *
 * Copyright 2011 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sleuthkit.autopsy.hashdatabase;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.datamodel.TskException;

/**
 * This class exists as a stop-gap measure to force users to have an indexed database.
 * This dialog pops up when the user attempts to exit the HashDbManagementPanel with unindexed databases loaded.
 * This class seeks to index the databases before allowing the user to move on, limiting system complexity. 
 * 
 * Although there is a cancel button, it exists in a zombie state. 
 * It removes the databases from the list, disallowing the user to use them for ingest, 
 * but does not kill the indexing process, leaving it to run in the background until completion.
 * Furthermore, it does not delete any files left over from a half-indexed state, forcing the user to perform cleanup. 
 */
class ModalNoButtons extends javax.swing.JDialog implements PropertyChangeListener {

    List<HashDb> unindexed;
    HashDb toIndex;
    HashSetsConfigurationPanel hdbmp;
    int length = 0;
    int currentcount = 1;
    String currentDb = "";

    /**
     * Creates a new ModalNoButtons with a list of unindexed databases.
     * @param hdbmp The panel that called this constructor. Needed to remove unindexed databases from the list.
     * @param parent Swing parent frame.
     * @param unindexed the list of unindexed databases to index.
     */
    ModalNoButtons(HashSetsConfigurationPanel hdbmp, java.awt.Frame parent, List<HashDb> unindexed) {
        super(parent, "Indexing databases", true);
        this.unindexed = unindexed;
        this.toIndex = null;
        this.hdbmp = hdbmp;
        initComponents();
        initCustom();
    }
    
    /**
     * Creates a new ModalNoButtons with a single unindexed hash database.
     * @param hdbmp The panel that called this constructor. Needed to remove unindexed databases from the list.
     * @param parent Swing parent frame.
     * @param unindexed The unindexed database to index.
     */
     ModalNoButtons(HashSetsConfigurationPanel hdbmp, java.awt.Frame parent, HashDb unindexed){
        super(parent, "Indexing database", true);
        this.unindexed = null;
        this.toIndex = unindexed;
        this.hdbmp = hdbmp;
        initComponents();
        initCustom();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        INDEXING_PROGBAR = new javax.swing.JProgressBar();
        GO_GET_COFFEE_LABEL = new javax.swing.JLabel();
        CURRENTLYON_LABEL = new javax.swing.JLabel();
        CURRENTDB_LABEL = new javax.swing.JLabel();
        CANCEL_BUTTON = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(519, 130));
        setMinimumSize(new java.awt.Dimension(519, 130));
        setModal(true);
        setPreferredSize(new java.awt.Dimension(519, 130));
        setResizable(false);

        GO_GET_COFFEE_LABEL.setDisplayedMnemonic('H');
        org.openide.awt.Mnemonics.setLocalizedText(GO_GET_COFFEE_LABEL, org.openide.util.NbBundle.getMessage(ModalNoButtons.class, "ModalNoButtons.GO_GET_COFFEE_LABEL.text")); // NOI18N

        CURRENTLYON_LABEL.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(CURRENTLYON_LABEL, org.openide.util.NbBundle.getMessage(ModalNoButtons.class, "ModalNoButtons.CURRENTLYON_LABEL.text")); // NOI18N

        CURRENTDB_LABEL.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(CURRENTDB_LABEL, org.openide.util.NbBundle.getMessage(ModalNoButtons.class, "ModalNoButtons.CURRENTDB_LABEL.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(CANCEL_BUTTON, org.openide.util.NbBundle.getMessage(ModalNoButtons.class, "ModalNoButtons.CANCEL_BUTTON.text")); // NOI18N
        CANCEL_BUTTON.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CANCEL_BUTTONMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(INDEXING_PROGBAR, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(GO_GET_COFFEE_LABEL)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(CURRENTLYON_LABEL)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CURRENTDB_LABEL)))
                        .addGap(0, 161, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(CANCEL_BUTTON, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(GO_GET_COFFEE_LABEL)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CURRENTLYON_LABEL)
                    .addComponent(CURRENTDB_LABEL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(INDEXING_PROGBAR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CANCEL_BUTTON)
                .addGap(36, 36, 36))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * When the cancel button is pressed, the dialog first seeks to confirm the event with the user,
     * then removes the databases from the HashDbManagementPanel list.
     * 
     * This method is imperfect, as it fails to cleanup residual files that were generated, 
     * as well as having a few other miscellaneous errors.
     * 
     * @param evt mouse click event
     */
    private void CANCEL_BUTTONMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CANCEL_BUTTONMouseClicked
        // TODO add your handling code here:
        String message = "You are about to exit out of indexing your hash databases. \n"
                + "The generated index will be left unusable. If you choose to continue,\n "
                + "please delete the corresponding -md5.idx file in the hash folder.\n"
                + "                                                Exit indexing?";

        int res = JOptionPane.showConfirmDialog(this, message, "Unfinished Indexing", JOptionPane.YES_NO_OPTION);
        if(res == JOptionPane.YES_OPTION){
            List<HashDb> remove = new ArrayList<HashDb>();
            if(this.toIndex == null){
                remove = this.unindexed;
            }
            else{
                remove.add(this.toIndex);
            }
            this.hdbmp.removeThese(remove);
            this.setVisible(false);
            this.setModal(false);
            this.dispose();
          }
        
    }//GEN-LAST:event_CANCEL_BUTTONMouseClicked

    private void initCustom() {
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        if(this.unindexed != null){
            indexThese();
        }
        else{
            indexThis();
        }
    }

    /**
     * Takes the singular HashDB and indexes it, setting various text labels along the way.
     */
    private void indexThis() {
        this.INDEXING_PROGBAR.setIndeterminate(true);
        currentDb = this.toIndex.getDisplayName();
        this.CURRENTDB_LABEL.setText("(" + currentDb + ")");
        this.length = 1;
        this.CURRENTLYON_LABEL.setText("Currently indexing 1 database");
        if (!this.toIndex.isIndexing()) {
            this.toIndex.addPropertyChangeListener(this);
            try {
                this.toIndex.createIndex();
            } catch (TskException se) {
                Logger.getLogger(ModalNoButtons.class.getName()).log(Level.WARNING, "Error making TSK index", se);
            }
        }
    }
    
    /**
     * Takes the list of unindexed databases and indexes them, setting various text labels along the way.
     */
    private void indexThese() {
        length = this.unindexed.size();
        this.INDEXING_PROGBAR.setIndeterminate(true);
        for (HashDb db : this.unindexed) {
            currentDb = db.getDisplayName();
            this.CURRENTDB_LABEL.setText("(" + currentDb + ")");
            this.CURRENTLYON_LABEL.setText("Currently indexing 1 of " + length);
            if (!db.isIndexing()) {
                db.addPropertyChangeListener(this);
                try {
                    db.createIndex();
                } catch (TskException e) {
                    Logger.getLogger(ModalNoButtons.class.getName()).log(Level.WARNING, "Error making TSK index", e);
                }
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CANCEL_BUTTON;
    private javax.swing.JLabel CURRENTDB_LABEL;
    private javax.swing.JLabel CURRENTLYON_LABEL;
    private javax.swing.JLabel GO_GET_COFFEE_LABEL;
    private javax.swing.JProgressBar INDEXING_PROGBAR;
    // End of variables declaration//GEN-END:variables

    @Override
    /**
     * Displays the current count of indexing when one is completed, or kills this dialog if all indexing is complete.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(HashDb.Event.INDEXING_DONE.name())) {
            if (currentcount >= length) {
                this.INDEXING_PROGBAR.setValue(100);
                this.setModal(false);
                this.setVisible(false);
                this.dispose();
            } else {
                currentcount++;
                this.CURRENTLYON_LABEL.setText("Currently indexing " + currentcount + " of " + length);
                
            }
        }
    }
}
