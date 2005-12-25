package org.grlea.games.hl.decal.gui;

// $Id: JCurvedLabel.java,v 1.1 2005-12-25 22:10:09 grlea Exp $
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

import javax.swing.JComponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.QuadCurve2D;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
JCurvedLabel
extends JComponent
{
   private static final Font DEFAULT_FONT = new Font("Sans Serif", Font.BOLD, 32);

   private final String text;

   private int padding = 10;
//   private int padding = 0;

   private int orientation = 1;

   public
   JCurvedLabel(String text)
   {
      this.text = text;
      setOpaque(false);
      setFont(DEFAULT_FONT);
      setBackground(new Color(70, 60, 180));
      setForeground(Color.white);

//      setSize(getPreferredSize());
   }

   public void
   setSize(int width, int height)
   {
      super.setSize(width, height);
   }

   public Dimension
   getMaximumSize()
   {
      return getGraphicsConfiguration().getBounds().getSize();
   }

   public Dimension
   getMinimumSize()
   {
      return getPreferredSize();
   }

   public Dimension
   getPreferredSize()
   {
      FontMetrics fontMetrics = getGraphics().getFontMetrics(getFont());
      int stringWidth = fontMetrics.stringWidth(text);
      int stringHeight =
         fontMetrics.getMaxAdvance() + fontMetrics.getMaxDescent();

      int buffer = padding * 2;
      int width = stringWidth + buffer;
      int height = stringHeight + buffer;

      if (orientation == 1)
      {
         width ^= height;
         height ^= width;
         width ^= height;
         height ^= width;
      }

      return new Dimension(width, height);
   }

   protected void
   paintComponent(Graphics g)
   {
      super.paintComponent(g);

      Graphics2D graphics = (Graphics2D) g;

      int width = getWidth();
      int height = getHeight();
      if (orientation == 1)
      {
         width = height;
         graphics.translate(0, height);
         graphics.rotate(-Math.PI / 2);
      }

      FontMetrics fontMetrics = getGraphics().getFontMetrics(getFont());
      int stringHeight = fontMetrics.getAscent() + fontMetrics.getDescent();

      stringHeight += padding;

      GeneralPath path = new GeneralPath();
      float curveHeight = stringHeight * 1.5F;
      path.moveTo(width, curveHeight);
      path.lineTo(width, 0);
      path.lineTo(0, 0);
      path.lineTo(0, curveHeight);
      path.append(new QuadCurve2D.Float(0, curveHeight, width / 4, curveHeight - stringHeight / 3, width / 2, curveHeight), false);
      path.append(new QuadCurve2D.Float(width / 2, curveHeight, width * 3 / 4, curveHeight + stringHeight / 3, width, curveHeight), false);

      graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
      graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      graphics.setColor(getBackground());
      graphics.fill(path);

      graphics.setColor(getForeground());
      graphics.drawString(text, padding, padding + fontMetrics.getAscent());
   }
}
