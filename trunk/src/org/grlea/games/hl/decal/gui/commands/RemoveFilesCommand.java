package org.grlea.games.hl.decal.gui.commands;

// $Id: RemoveFilesCommand.java,v 1.1 2005-12-25 22:10:12 grlea Exp $
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

import org.grlea.games.hl.decal.gui.Model;
import org.grlea.games.hl.decal.gui.SourceFile;
import org.grlea.games.hl.decal.gui.SourceFileList;
import org.grlea.log.DebugLevel;
import org.grlea.log.SimpleLogger;

import org.pietschy.command.ActionCommand;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
RemoveFilesCommand
extends ActionCommand implements SourceFileList.Listener, ListSelectionListener
{
   private static final SimpleLogger log = new SimpleLogger(RemoveFilesCommand.class);

   private Model model;

   private final ListSelectionModel listSelectionModel;

   public
   RemoveFilesCommand(Model model, ListSelectionModel listSelectionModel)
   {
      super("RemoveFiles");

      this.model = model;
      this.listSelectionModel = listSelectionModel;

      model.getFileList().addListener(this);
      listSelectionModel.addListSelectionListener(this);
      updateEnabledState();
   }

   protected void
   handleExecute()
   {
      log.entry("handleExecute()");

      int minIndex = listSelectionModel.getMinSelectionIndex();
      int maxIndex = listSelectionModel.getMaxSelectionIndex();
      log.dbo(DebugLevel.L6_VERBOSE, "minIndex", minIndex);
      log.dbo(DebugLevel.L6_VERBOSE, "maxIndex", maxIndex);

      if (minIndex != -1 && maxIndex != -1)
      {
         int numberOfSelectedFiles = maxIndex - minIndex + 1;
         ArrayList filesToRemove = new ArrayList(numberOfSelectedFiles);

         SourceFileList fileList = model.getFileList();
         if (fileList.getSize() != 0)
         {
            for (int fileIndex = minIndex; fileIndex <= maxIndex && fileIndex < fileList.getSize(); fileIndex++)
            {
               filesToRemove.add(fileList.getFile(fileIndex));
            }

            for (Iterator iterator = filesToRemove.iterator(); iterator.hasNext();)
            {
               fileList.removeFile((SourceFile) iterator.next());
            }
         }

         listSelectionModel.clearSelection();
      }

      log.exit("handleExecute()");
   }

   public void
   fileAdded(SourceFile file)
   {
      updateEnabledState();
   }

   public void
   fileRemoved(SourceFile file)
   {
      updateEnabledState();
   }

   public void
   valueChanged(ListSelectionEvent e)
   {
      updateEnabledState();
   }

   private void
   updateEnabledState()
   {
      if (listSelectionModel.getValueIsAdjusting())
         return;

      boolean modelContainsFiles = model.getFileList().getSize() != 0;
      boolean listHasASelection = listSelectionModel.getMinSelectionIndex() != -1;
      setEnabled(modelContainsFiles && listHasASelection);
   }
}