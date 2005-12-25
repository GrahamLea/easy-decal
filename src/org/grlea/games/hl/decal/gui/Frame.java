package org.grlea.games.hl.decal.gui;

// $Id: Frame.java,v 1.1 2005-12-25 22:10:09 grlea Exp $
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

import org.grlea.log.SimpleLogger;

import org.pietschy.command.ActionCommand;
import org.pietschy.command.LoadException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
Frame
extends JFrame
{
   private static final SimpleLogger log = new SimpleLogger(Frame.class);

   private final GuiResources guiResources = GuiResources.instance();

   public
   Frame()
   throws LoadException
   {
      setTitle(guiResources.frameTitle());

      try
      {
         BufferedImage iconImage = ImageIO.read(getClass().getResource("/ED_Metal_32.png"));
         setIconImage(iconImage);
      }
      catch (IOException e)
      {
         log.warn("Failed to load icon");
         log.warnException(e);
      }

      Model model = new Model();
      final EasyDecalPanel panel = new EasyDecalPanel(model);

      Container contentPane = getContentPane();
      contentPane.setLayout(new BorderLayout());
      contentPane.add(new JCurvedLabel("Easy Decal"), BorderLayout.WEST);
      contentPane.add(panel, BorderLayout.CENTER);
      pack();
      getToolkit().setDynamicLayout(true);
//      setSize((int) (getWidth() * 1.2), getHeight());
      setLocationRelativeTo(null);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      addWindowListener(new WindowAdapter()
      {
         public void
         windowClosed(WindowEvent e)
         {
            ActionCommand exitCommand = panel.getExitCommand();
            if (exitCommand.isEnabled())
               exitCommand.execute();
         }
      });
   }

   public static void
   startEasyDecalGui()
   {
      log.entry("main()");

      try
      {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch (Exception e)
      {
         log.error("Failed to set Look and Feel");
         log.errorException(e);
      }

      try
      {
         EventQueue.invokeAndWait(new Runnable()
         {
            public void
            run()
            {
               try
               {
                  JFrame frame = new Frame();
                  frame.setVisible(true);
               }
               catch (LoadException e)
               {
                  String message = GuiResources.instance().failedToInitialiseGuiCommands();
                  log.fatal(message);
                  log.fatalException(e);
                  String dialogMessage =
                     "<html>Easy Decal:<br>" + message + "<br>" + e.toString() + "</html>";
                  JOptionPane.showMessageDialog(null, dialogMessage,
                                                GuiResources.instance().fatalErrorDialogTitle(),
                                                JOptionPane.ERROR_MESSAGE);
                  System.exit(1);
               }
            }
         });
      }
      catch (Exception e)
      {
         String message = GuiResources.instance().failedToInitialiseSwing();
         log.fatal(message);
         log.fatalException(e);
         String dialogMessage = "<html>Easy Decal:<br>" + message + "<br>" + e.toString() + "</html>";
         JOptionPane.showMessageDialog(null, dialogMessage,
                                       GuiResources.instance().fatalErrorDialogTitle(),
                                       JOptionPane.ERROR_MESSAGE);
         System.exit(1);
      }
   }
}