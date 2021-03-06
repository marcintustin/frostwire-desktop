/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011-2014, FrostWire(R). All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.limegroup.gnutella.gui.search;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.limegroup.gnutella.gui.GUIMediator;

/**
 * 
 * @author gubatron
 * @author aldenml
 *
 */
final class SearchTabbedPane extends JTabbedPane {

    public SearchTabbedPane() {
    }

    @Override
    public void addTab(String title, Icon icon, Component component) {
        super.addTab(title, icon, component);

        int tabIndex = getTabCount() - 1;
        setTabComponentAt(tabIndex, new SearchTabHeader(component, title));
    }

    @Override
    public void setTitleAt(int index, String title) {
        Component c = getTabComponentAt(index);
        if (c instanceof SearchTabHeader) {
            ((SearchTabHeader) c).setTitle(title);
        }
    }

    public void setProgressActiveAt(int index, boolean active) {
        Component c = getTabComponentAt(index);
        if (c instanceof SearchTabHeader) {
            ((SearchTabHeader) c).setProgressActive(active);
        }
    }
    
    public void switchToTabByOffset(int offset) {
      int oldIndex = (getSelectedIndex()<0) ? 0 : getSelectedIndex();
      int newIndex = (oldIndex+offset) % getTabCount();
      //java's modulo will return negative numbers... damn you Gosling.
      if (newIndex < 0) {
          newIndex += getTabCount();
      }
      setSelectedIndex(newIndex);  
    }

    private final class SearchTabHeader extends JPanel {

        private final Component component;
        private final JButton buttonClose;
        private final JLabel labelText;

        public SearchTabHeader(Component component, String text) {
            this.component = component;

            setLayout(new MigLayout("insets 0, gap 0"));

            buttonClose = new JButton(CancelSearchIconProxy.createSelected());
            buttonClose.setOpaque(false);
            buttonClose.setContentAreaFilled(false);
            buttonClose.setBorderPainted(false);
            buttonClose.addActionListener(new CloseActionHandler());
            add(buttonClose, "h 17!, w 23!");

            labelText = new JLabel(text.trim());
            labelText.setHorizontalTextPosition(SwingConstants.LEADING);
            labelText.setAlignmentX(SwingConstants.RIGHT);
            labelText.setIcon(GUIMediator.getThemeImage("indeterminate_small_progress"));
            add(labelText);
        }

        public void setTitle(String title) {
            labelText.setText(title);
        }

        public void setProgressActive(boolean active) {
            if (active) {
                labelText.setIcon(GUIMediator.getThemeImage("indeterminate_small_progress"));
            } else {
                labelText.setIcon(null);
            }
        }

        public class CloseActionHandler implements ActionListener {

            public CloseActionHandler() {
            }

            public void actionPerformed(ActionEvent evt) {
                int index = SearchMediator.getSearchResultDisplayer().getIndexForTabComponent(component);
                if (index != -1) {
                    SearchMediator.getSearchResultDisplayer().killSearchAtIndex(index);
                }
            }
        }
    }
}
