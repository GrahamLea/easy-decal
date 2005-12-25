package org.grlea.games.hl.decal.gui;

// $Id: DonatePanel.java,v 1.1 2005-12-25 22:10:07 grlea Exp $
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

import org.grlea.games.hl.decal.gui.commands.DonateCommand;

import org.pietschy.explicit.TableBuilder;

import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
DonatePanel
extends JPanel
{
   public
   DonatePanel()
   {
      JLabel messageLabel = new JLabel(GuiResources.instance().donateMessage(), JLabel.CENTER);

      DonateCommand donateCommand = new DonateCommand();
      final AbstractButton donateButton = donateCommand.createButton();

      TableBuilder builder = new TableBuilder(this);
      int row = 0;
      builder.add(new JLabel(" "), row++, 0);
      builder.add(messageLabel, row++, 0);
      builder.add(new JLabel(" "), row++, 0);
      builder.add(donateButton, row++, 0);
      builder.add(new JLabel(" "), row++, 0);

      builder.column(0).alignCentre().grow(1);
      builder.buildLayout();

      addComponentListener(new ComponentAdapter()
      {
         public void
         componentShown(ComponentEvent e)
         {
            donateButton.requestFocus();
         }
      });
   }
}