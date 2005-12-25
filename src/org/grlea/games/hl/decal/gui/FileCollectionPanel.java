package org.grlea.games.hl.decal.gui;

// $Id: FileCollectionPanel.java,v 1.1 2005-12-25 22:10:08 grlea Exp $
// Copyright (c) 2004 Graham Lea. All rights reserved.

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import org.grlea.games.hl.decal.gui.commands.AddFilesCommand;
import org.grlea.games.hl.decal.gui.commands.RemoveFilesCommand;

import org.pietschy.explicit.TableBuilder;

import javax.swing.AbstractButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
FileCollectionPanel
extends JPanel
{
   public
   FileCollectionPanel(Model model)
   {
      FileCollectionListModel listModel = new FileCollectionListModel(model);
      JList list = new JList(listModel);
      list.setCellRenderer(new SourceFileRenderer());
      ListSelectionModel listSelectionModel = list.getSelectionModel();
      listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

      JScrollPane listScrollPane = new JScrollPane(list);
      listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      listScrollPane.getVerticalScrollBar().setFocusable(false);

      AddFilesCommand addFilesCommand = new AddFilesCommand(model);
      RemoveFilesCommand removeFilesCommand = new RemoveFilesCommand(model, listSelectionModel);

      AbstractButton addButton = addFilesCommand.createButton();
      AbstractButton removeButton = removeFilesCommand.createButton();

      TableBuilder builder = new TableBuilder(this);

      builder.addXY(listScrollPane, 0, 0, 1, 3).fill().getLastRow().grow(1);
      builder.addXY(addButton, 2, 0).fillX();
      builder.addXY(removeButton, 2, 1).fillX();

      builder.column(0).grow(1);
      builder.buildLayout();
   }
}