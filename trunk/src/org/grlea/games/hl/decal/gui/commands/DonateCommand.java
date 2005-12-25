package org.grlea.games.hl.decal.gui.commands;

// $Id: DonateCommand.java,v 1.1 2005-12-25 22:10:12 grlea Exp $
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

import org.grlea.games.hl.decal.gui.GuiResources;
import org.grlea.log.SimpleLogger;

import org.jdesktop.jdic.desktop.Desktop;
import org.pietschy.command.ActionCommand;
import org.pietschy.explicit.TableBuilder;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * <p></p>
 *
 * @author Graham Lea
 * @version $Revision: 1.1 $
 */
public class
DonateCommand
extends ActionCommand
{
   private static final SimpleLogger log = new SimpleLogger(DonateCommand.class);

   private static final String DONATE_URL = "http://www.grlea.org/easydecal/donate";

   public
   DonateCommand()
   {
      super("Donate");
   }

   protected void
   handleExecute()
   {
      try
      {
         Desktop.browse(new URL(DONATE_URL));
      }
      catch (Throwable e)
      {
         Window invoker = getInvokerWindow();
         Frame invokerFrame = (invoker instanceof Frame) ? (Frame) invoker : null;
         final JDialog dialog = new JDialog(invokerFrame, "Error", false);
         // TODO (grahaml) I18n
         JLabel label =
            new JLabel(GuiResources.instance().failedToOpenBrowser());
         JTextField textField = new JTextField(DONATE_URL);

         // TODO (grahaml) I18n
         JButton closeButton = new JButton("Close");
         closeButton.addActionListener(new ActionListener()
         {
            public void
            actionPerformed(ActionEvent e)
            {
               dialog.setVisible(false);
            }
         });

         TableBuilder builder = new TableBuilder();
         builder.add(label, 0, 0).alignCentre();
         builder.add(textField, 1, 0).fill();
         builder.add(closeButton, 2, 0).alignCentre();
         builder.margin(builder.layoutStyle().dialogMargin());
         builder.column(0).grow(1);
         builder.buildLayout();

         dialog.getContentPane().add(builder.getPanel());

         dialog.pack();
         dialog.setSize((int) (dialog.getWidth() * 1.15), dialog.getHeight());
         dialog.setLocationRelativeTo(invoker);
         dialog.setVisible(true);
      }
   }
}