package org.grlea.games.hl.decal.gui;

// $Id: JHyperlink.java,v 1.1 2005-12-25 22:10:09 grlea Exp $
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

import javax.swing.JLabel;
import javax.swing.event.EventListenerList;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventListener;

/**
 * <p></p>
 *
 * @author Graham Lea
 * @version $Revision: 1.1 $
 */
public class 
JHyperlink
extends JLabel
{
   private static volatile int nextEventId = 0;

   private final EventListenerList listenerList = new EventListenerList();

   public
   JHyperlink(String linkText)
   {
      super("<html><a href=\"#\">" +  linkText + "<a></html>");
      setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      addMouseListener(new MouseAdapter()
      {
         public void
         mouseClicked(MouseEvent e)
         {
            fireActionEvent(new ActionEvent(JHyperlink.this, nextEventId++, "Click"));
         }
      });
   }

   public void
   addActionListener(ActionListener listener)
   {
      listenerList.add(ActionListener.class, listener);
   }

   public void
   removeActionListener(ActionListener listener)
   {
      listenerList.remove(ActionListener.class, listener);
   }

   private void
   fireActionEvent(ActionEvent event)
   {
      EventListener[] listeners = listenerList.getListeners(ActionListener.class);
      for (int i = 0; i < listeners.length; i++)
      {
         EventListener listener = listeners[i];
         ((ActionListener) listener).actionPerformed(event);
      }
   }
}