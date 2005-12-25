package org.grlea.games.hl.decal.gui;

// $Id: SourceFileRenderer.java,v 1.1 2005-12-25 22:10:11 grlea Exp $
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

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import java.awt.Component;
import java.io.File;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
SourceFileRenderer
extends DefaultListCellRenderer
{
   public
   SourceFileRenderer()
   {}

   public Component
   getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
   {
      JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

      SourceFile sourceFile = (SourceFile) value;
      File file = sourceFile.getFile();
      label.setText(file.getName());
      label.setToolTipText(file.getAbsoluteFile().getParent());
      return label;
   }
}