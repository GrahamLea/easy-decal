package org.grlea.games.hl.decal.gui;

// $Id: EasyDecalPanel.java,v 1.1 2005-12-25 22:10:08 grlea Exp $
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

import org.grlea.games.hl.decal.gui.commands.CreateDecalsCommand;
import org.grlea.games.hl.decal.gui.commands.ExitCommand;
import org.grlea.log.SimpleLogger;

import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;
import org.pietschy.command.LoadException;
import org.pietschy.explicit.PreferredWidthMaxVisitor;
import org.pietschy.explicit.TableBuilder;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import java.awt.*;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.net.URL;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
EasyDecalPanel
extends JPanel
{
   private static final SimpleLogger log = new SimpleLogger(EasyDecalPanel.class);

   private final ExitCommand exitCommand;

   private final AbstractButton createDecalsButton;
   private final CardLayout cardLayout;
   private final JPanel cardPanel;
   private final String decalCreationPanelName;
   private final String donatePanelName;

   public
   EasyDecalPanel(Model model)
   throws LoadException
   {
      log.entry("EasyDecalPanel()");

      CommandManager commandManager = CommandManager.defaultInstance();
      commandManager.setResourceBundle(GuiResources.instance().getResourceBundle());
      URL commandFile = getClass().getResource("gui-commands.xml");
      log.debugObject("commandFile", commandFile);
      commandManager.load(commandFile);

      CreateDecalsCommand createDecalsCommand = new CreateDecalsCommand(model);
      createDecalsButton = createDecalsCommand.createButton();

      exitCommand = new ExitCommand();
      AbstractButton exitButton = exitCommand.createButton();

      FileCollectionPanel fileCollectionPanel = new FileCollectionPanel(model);

      DecalCreationPanel decalCreationPanel1 = new DecalCreationPanel(model);
      JPanel decalCreationPanel = new JPanel(new BorderLayout());
      decalCreationPanel.add(decalCreationPanel1, BorderLayout.CENTER);

      DonatePanel donatePanel = new DonatePanel();

      // Button panel
      JPanel buttonPanel = new JPanel();
      {
         TableBuilder builder = new TableBuilder(buttonPanel);
         builder.addXY(createDecalsButton, 1, 0);
         builder.addXY(exitButton, 2, 0);
         builder.visitCells(new PreferredWidthMaxVisitor(new Component[] {createDecalsButton, exitButton}));
         builder.buildLayout();
      }

      // CardLayout panel
      final String fileCollectionPanelName = fileCollectionPanel.getClass().getName();
      decalCreationPanelName = decalCreationPanel.getClass().getName();
      donatePanelName = donatePanel.getClass().getName();

      cardLayout = new CardLayout();
      cardPanel = new JPanel(cardLayout);
      cardPanel.add(fileCollectionPanel, fileCollectionPanelName);
      cardPanel.add(decalCreationPanel, decalCreationPanelName);
      cardPanel.add(donatePanel, donatePanelName);

      // this Layout
      TableBuilder builder = new TableBuilder(this);
      builder.addXY(cardPanel, 0, 0, 2, 1).fill().getFirstRow().grow(1);
      builder.addXY(new JSeparator(), 0, 1, 2, 1).fillX();
      builder.addXY(buttonPanel, 1, 2).alignRight().getLastColumn().grow(1);

      builder.margin(builder.layoutStyle().dialogMargin());
      builder.buildLayout();

      // GUI Response
      model.getProcessor().addListener(new ProcessorListener());

      log.exit("EasyDecalPanel()");
   }

   ActionCommand
   getExitCommand()
   {
      return exitCommand;
   }

   private class
   ProcessorListener
   extends Processor.Listener
   {
      private File currentFile;

      private final List errors = new ArrayList(2);

      public
      ProcessorListener()
      {
      }

      public void
      starting()
      {
         exitCommand.setEnabled(false);
         createDecalsButton.setVisible(false);
         cardLayout.show(cardPanel, decalCreationPanelName);
      }

      public void
      c1_StartingFile(File file)
      {
         currentFile = file;
      }

      public void
      allDone()
      {
         try
         {
            Thread.sleep(500);
         }
         catch (InterruptedException ie)
         {
            // Ignore
         }

         if (!errors.isEmpty())
         {
            Frame frame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, EasyDecalPanel.this);
            ErrorsDialog errorsDialog = new ErrorsDialog(frame, errors);
            errorsDialog.setLocationRelativeTo(frame);
            errorsDialog.setVisible(true);
         }

         exitCommand.setEnabled(true);
         cardLayout.show(cardPanel, donatePanelName);
         requestFocus();
         Window window =
            (Window) SwingUtilities.getAncestorOfClass(Window.class, EasyDecalPanel.this);
         if (window != null)
         {
            window.toFront();
         }
      }

      public void
      error(String error)
      {
         errors.add(new DecalError(currentFile, error));
      }

      public void
      uncaughtError(Throwable t)
      {
         String message = "<html>Easy Decal has incurred an internal error.<br><br>" +
                          "Please report this to issues@easy-decal.dev.java.net<br><br>" + t;
         JOptionPane.showMessageDialog(EasyDecalPanel.this, message, "Internal Error",
                                       JOptionPane.ERROR_MESSAGE);
      }
   }

   private static final class
   DecalError
   {
      private final File file;
      private final String errorMessage;

      public
      DecalError(File file, String errorMessage)
      {
         this.file = file;
         this.errorMessage = errorMessage;
      }
   }

   private static final class
   ErrorsDialog
   extends JDialog
   {
      public
      ErrorsDialog(Frame owner, List errors)
      throws HeadlessException
      {
         super(owner, GuiResources.instance().errorsDialogTitle());

         JLabel headerLabel = new JLabel(GuiResources.instance().errorsDialogHeader());

         List formattedErrorsList = new ArrayList(errors.size());

         for (Iterator iterator = errors.iterator(); iterator.hasNext();)
         {
            DecalError error = (DecalError) iterator.next();

            String directory =
               (error.file.getParent() != null) ? error.file.getParentFile().getPath() : "";

            int myDocumentsIndex = directory.indexOf("My Documents");
            if (myDocumentsIndex != -1)
               directory = directory.substring(myDocumentsIndex);

            String errorMessage =
               "<html><b><font color=\"#333333\">" + error.file.getName() + "</font></b><br>" +
               "(" + directory + ")<br>" +
               "<b><font color=\"#990000\">" + error.errorMessage + "</font></b><br></html>";

            formattedErrorsList.add(errorMessage);
         }

         JList errorList =
            new JList(formattedErrorsList.toArray(new String[formattedErrorsList.size()]));
         errorList.setVisibleRowCount(4);
         errorList.setSelectionForeground(errorList.getForeground());
         errorList.setSelectionBackground(errorList.getBackground());

         JScrollPane errorScrollPane = new JScrollPane(errorList);
         errorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

         JPanel panel = new JPanel();
         TableBuilder builder = new TableBuilder(panel);
         int row = 0;
         builder.add(headerLabel, row++, 0);
         builder.add(errorScrollPane, row++, 0);

         JButton closeButton = new JButton(GuiResources.instance().errorsDialogCloseButton());
         closeButton.addActionListener(new ActionListener()
         {
            public void
            actionPerformed(ActionEvent e)
            {
               dispose();
            }
         });

         builder.add(closeButton, row++, 0).alignCentre();

         builder.margin(builder.layoutStyle().dialogMargin());
         builder.buildLayout();

         Container contentPane = getContentPane();
         contentPane.setLayout(new BorderLayout());
         contentPane.add(panel, BorderLayout.CENTER);

         pack();

         setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      }
   }
}