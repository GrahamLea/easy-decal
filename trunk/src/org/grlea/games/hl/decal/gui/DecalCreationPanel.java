package org.grlea.games.hl.decal.gui;

// $Id: DecalCreationPanel.java,v 1.1 2005-12-25 22:10:07 grlea Exp $
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

import org.pietschy.explicit.TableBuilder;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import java.awt.Font;
import java.io.File;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
DecalCreationPanel
extends JPanel
{
   private final JLabel filenameLabel = new JLabel();
   private final JLabel directoryLabel = new JLabel();

   private final JProgressBar progressBar;

   public
   DecalCreationPanel(final Model model)
   {
      Font filenameLabelFont = filenameLabel.getFont();
      filenameLabel.setFont(filenameLabelFont.deriveFont(filenameLabelFont.getSize() * 1.5F));

      progressBar = new JProgressBar();
      progressBar.setStringPainted(true);

      // Layout
      TableBuilder builder = new TableBuilder(this);
      int row = 0;

      JLabel spacer = new JLabel();
      spacer.setFont(filenameLabelFont);
      builder.add(spacer, row++, 0);
      builder.add(filenameLabel, row++, 0);
      builder.add(directoryLabel, row++, 0);

      builder.add(new JLabel(), row++, 0);

      builder.add(progressBar, row++, 0).fillX();

      builder.column(0).grow(1);
      builder.buildLayout();

      // Listening
      model.getProcessor().addListener(new Listener(model));
   }

   private class
   Listener
   extends Processor.Listener
   {
      private final Model model;

      public Listener(Model model)
      {
         this.model = model;
      }

      public void
      starting()
      {
         progressBar.setMinimum(0);
         progressBar.setMaximum(model.getFileList().getSize());
         setProgressBarString();
      }

      public void
      c1_StartingFile(File file)
      {
         filenameLabel.setText(file.getName());
         File parentFile = file.getAbsoluteFile().getParentFile();
         String parentFileName = parentFile != null ? parentFile.toString() : "";

         String userDir = System.getProperty("user.home");
         if (userDir != null && !userDir.endsWith(File.separator))
            userDir += File.separator;

         if (userDir != null && parentFileName.startsWith(userDir))
         {
            parentFileName = parentFileName.substring(userDir.length());
         }

         directoryLabel.setText(parentFileName);
      }

      public void
      c7_ImageDone()
      {
         progressBar.setValue(progressBar.getValue() + 1);
         setProgressBarString();
      }

      private void
      setProgressBarString()
      {
         progressBar.setString(GuiResources.instance().progressBarString(
                                    progressBar.getValue(), progressBar.getMaximum()));
      }
   }
}