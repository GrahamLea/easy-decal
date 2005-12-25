package org.grlea.games.hl.decal.gui.commands;

// $Id: AddFilesCommand.java,v 1.1 2005-12-25 22:10:11 grlea Exp $
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
import org.grlea.graphics.ImageFileFilter;
import org.grlea.log.DebugLevel;
import org.grlea.log.SimpleLogger;

import org.pietschy.command.ActionCommand;

import javax.swing.JFileChooser;

import java.io.File;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
AddFilesCommand
extends ActionCommand
{
   private static final SimpleLogger log = new SimpleLogger(AddFilesCommand.class);

   private final Model model;

   private final JFileChooser fileChooser;

   public
   AddFilesCommand(Model model)
   {
      super("AddFiles");

      this.model = model;

      fileChooser = new JFileChooser();
      fileChooser.setFileFilter(new ImageFileFilter());
      fileChooser.setMultiSelectionEnabled(true);
   }

   protected void
   handleExecute()
   {
      log.entry("handleExecute()");

      int result = fileChooser.showOpenDialog(getInvokerWindow());
      if (result == JFileChooser.APPROVE_OPTION)
      {
         File[] selectedFiles = fileChooser.getSelectedFiles();
         SourceFileList fileList = model.getFileList();
         for (int i = 0; i < selectedFiles.length; i++)
         {
            File selectedFile = selectedFiles[i];
            log.dbo(DebugLevel.L5_DEBUG, "selectedFile", selectedFile);
            fileList.addFile(new SourceFile(selectedFile));
         }
      }

      log.exit("handleExecute()");
   }
}