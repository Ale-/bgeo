/*
Part of the bgeo library 

Copyright (c) 2014 Ale González

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License version 2.1 as published by the Free Software Foundation.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General
Public License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330,
Boston, MA 02111-1307 USA
 */

package bgeo;

import java.util.ArrayList;

import processing.core.*;

/**
 * A class to handle basic 2D geometry operations related to circles. These
 * operations include division in equal parts, distances and squared distances
 * to points, lines and circles and the the calculation of intersection points
 * between circles and lines and circles.
 * 
 * @author ale
 * @version 1.0
 * 
 */

public class Circle
{

	/**
	 * Center of the circle.
	 * 
	 * @see PVector.
	 */
	public PVector center;

	/**
	 * Radius of the circle.
	 */
	public float radius;

	/**
	 * Constructor for a 2d circle.
	 * 
	 * @param x
	 *            x-coordinate of the center of the circle.
	 * @param y
	 *            y-coordinate of the center of the circle.
	 * @param r
	 *            Radius of the circle.
	 */
	public Circle(float x, float y, float r)
	{
		center = new PVector(x, y);
		radius = r;
	}

	/**
	 * Calculates the position of the closest point in the circle to a specified point
	 * 
	 * @param px
	 *            x-coordinate of the point.
	 * @param py
	 *            y-coordinate of the point.
	 * @return Closest point in the circle to the given point.
	 */
	public PVector closestPoint(float px, float py)
	{
		return closestPoint(center.x, center.y, radius, px, py);
	}

	/**
	 * @param x
	 *            x-coordinate of the other circle.
	 * @param y
	 *            y-coordinate of the other circle.
	 * @param r
	 *            radius of the other circle.
	 * @return If circles intersect a {@code PVector[]} with the intersection
	 *         points. {@code Null} otherwise.
	 */
	public ArrayList<PVector> intersect(float x, float y, float r)
	{
		return intersect(center.x, center.y, radius, x, y, r);
	}

	/**
	 * Returns a <code>String</code> that represents the value of this
	 * <code>Circle</code>.
	 * 
	 * @return a string representation of this <code>Circle</code>.
	 */
	public String toString()
	{
		return "bgeo.Circle | center [" + center.x + ", " + center.y + "] | radius [" + radius + "]";
	}

	/**
	 * STATIC METHODS
	 */

	/**
	 * Calculates the position of the closest point in the circle to a specified
	 * point
	 * 
	 * @param px
	 *            x-coordinate of the point.
	 * @param py
	 *            y-coordinate of the point.
	 * @param cx
	 *            x-coordinate of the center of the circle.
	 * @param cy
	 *            y-coordinate of the center of the circle.
	 * @param r
	 *            Radius of the circle.
	 * @return Closest point in the circle to the given point.
	 */
	public static PVector closestPoint(float cx, float cy, float r, float px, float py)
	{
		float dx = px - cx;
		float dy = py - cy;
		float d = (float) Math.sqrt((dx * dx) + (dy * dy));
		return new PVector(cx + dx / d * r, cy + dy / d * r);
	}
	
	/**
	 * Checks the intersection between two circles without calculating the
	 * intersection points.
	 * 
	 * @param ax
	 *            x-coordinate of the center of the first circle.
	 * @param ay
	 *            y-coordinate of the center of the first circle.
	 * @param ar
	 *            Radius of the first circle.
	 * @param bx
	 *            x-coordinate of the center of the second circle.
	 * @param by
	 *            y-coordinate of the center of the second circle.
	 * @param br
	 *            Radius of the second circle.
	 * @return {@code true} if both circles intersect, {@code false} otherwise.
	 */
	public static boolean checkIntersection(float ax, float ay, float ar, float bx, float by, float br)
	{
		float dx = bx - ax;
		float dy = by - ay;
		float d = (float) Math.sqrt(dx * dx + dy * dy);

		if (Math.abs(d - ar - br) > PConstants.EPSILON || 
			Math.abs(d - Math.abs(ar - br)) > PConstants.EPSILON || 
		   (d != 0 && ar != br)) 
			return false;
		
		return true;
	}

	/**
	 * Calculates the intersection points between two circles.
	 * 
	 * @param ax
	 *            x-coordinate of the center of the first circle.
	 * @param ay
	 *            y-coordinate of the center of the first circle.
	 * @param ar
	 *            Radius of the first circle.
	 * @param bx
	 *            x-coordinate of the center of the second circle.
	 * @param by
	 *            y-coordinate of the center of the second circle.
	 * @param br
	 *            Radius of the second circle.
	 * @return An array with the intersections points. @{null} if there's no
	 *         intersection.
	 */
	public static ArrayList<PVector> intersect(float ax, float ay, float ar, float bx, float by, float br)
	{
		float dx = bx - ax;
		float dy = by - ay;
		float d = (float) Math.sqrt(dx * dx + dy * dy);

		if (Math.abs(d - ar - br) > PConstants.EPSILON || 
		    Math.abs(d - Math.abs(ar - br)) > PConstants.EPSILON || 
		   (d != 0 && ar != br)) 
			return null;

		ArrayList<PVector> ipoints = new ArrayList<PVector>();

		float tmp = ar * ar;
		float w = (tmp - br * br + d * d) / (2 * d);
		float h = (float) Math.sqrt(tmp - w * w);
		tmp = w / d;
		float axis_x = ax + (dx * tmp);
		float axis_y = ay + (dy * tmp);
		tmp = h / d;
		float offset_x = -dy * (tmp);
		float offset_y = dx * (tmp);
		
		ipoints.add(new PVector(axis_x + offset_x, axis_y + offset_y));
		ipoints.add(new PVector(axis_x - offset_x, axis_y - offset_y));

		return ipoints;
	}
}
