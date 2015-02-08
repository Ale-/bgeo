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
 * A class to handle basic 2D geometry operations related to lines. These
 * operations include division in equal parts, distances and squared distances
 * to points, lines and circles and the the calculation of intersection points
 * between two lines and a line and a circle.
 * 
 * @author ale
 * @version 1.0
 * 
 */

public class Line
{
	/**
	 * The first point of the segment.
	 * 
	 * @see PVector
	 */
	public PVector a;

	/**
	 * The second point of the segment.
	 * 
	 * @see PVector
	 */
	public PVector b;

	/**
	 * Constructor for a 2D line.
	 * 
	 * @param ax
	 *            x-coordinate of the beginning of the segment.
	 * @param ay
	 *            y-coordinate of the beginning of the segment.
	 * @param bx
	 *            x-coordinate of the ending of the segment.
	 * @param by
	 *            y-coordinate of the ending of the segment.
	 */
	public Line(float ax, float ay, float bx, float by)
	{
		a = new PVector(ax, ay);
		b = new PVector(bx, by);
	}

	/**
	 * Calculates the position of the closest point in the line to an external
	 * point.
	 * 
	 * @param px
	 *            x-coordinate of the external point.
	 * @param py
	 *            y-coordinate of the external point.
	 * @return Position of the closest point.
	 */
	public PVector closestPoint(float px, float py)
	{
		return closestPoint(a.x, a.y, b.x, b.y, px, py);
	}

	/**
	 * Calculates the position of the closest point in the line to an external
	 * point.
	 * 
	 * @param p
	 *            Position of the external point.
	 * 
	 * @return Position of the closest point.
	 */
	public PVector closestPoint(PVector p)
	{
		return closestPoint(a.x, a.y, b.x, b.y, p.x, p.y);
	}

	/**
	 * Calculates the intersection point with another line segment
	 * 
	 * @param ax
	 *            x-coordinate of the beginning of the other segment.
	 * @param ay
	 *            y-coordinate of the beginning of the other segment.
	 * @param bx
	 *            x-coordinate of the ending of the other segment.
	 * @param by
	 *            y-coordinate of the ending of the other segment.
	 * 
	 * @return Intersection point if both lines intersect. {@code null} in case
	 *         there's no intersection.
	 */
	public PVector intersect(float ax, float ay, float bx, float by)
	{
		return intersect(a.x, a.y, b.x, b.y, ax, ay, bx, by);
	}

	/**
	 * Calculates the intersection point with another line segment
	 * 
	 * @param l
	 *            The other segment
	 * 
	 * @return Intersection point if both lines intersect, {@code null} in case
	 *         there's no intersection
	 */
	public PVector intersect(Line l)
	{
		return intersect(a.x, a.y, b.x, b.y, l.a.x, l.a.y, l.b.x, l.b.y);
	}

	/**
	 * Calculates the intersection point with a circle.
	 * 
	 * @param cx
	 *            x-coordinate of the center of the circle.
	 * @param cy
	 *            y-coordinate of the center of the circle.
	 * @param r
	 *            Radius of the circle.
	 * 
	 * @return Arraylist of intersection points. {@code Null} If there is no intersection.
	 */
	public ArrayList<PVector> intersect(float cx, float cy, float r)
	{
		return intersect(a.x, a.y, b.x, b.y, cx, cy, r);
	}

	/**
	 * Calculates the intersection point with a circle.
	 * 
	 * @param o
	 *            The circle.
	 * @return Arraylist of intersection points. {@code Null} If there is no intersection.
	 */
	public ArrayList<PVector> intersect(Circle o)
	{
		return intersect(a.x, a.y, b.x, b.y, o.center.x, o.center.y, o.radius);
	}

	/**
	 * Divides the line in a number {@code n} of equal parts, returning an array
	 * with the division points
	 * 
	 * @param n
	 *            Number of equal parts to divide the segment.
	 * @return Array of division points.
	 */
	public PVector[] divide(int n)
	{
		return divide(a.x, a.y, b.x, b.y, n);
	}

	/**
	 * Returns a {@code String} that represents the value of this {@code Line}.
	 * 
	 * @return a string representation of this <code>Line</code>.
	 */
	public String toString()
	{
		return "bgeo.Line[" + a.x + ", " + a.y + "][" + b.x + ", " + b.y + "]";
	}

	/**
	 * STATIC METHODS
	 */

	/**
	 * Calculates the position of the closest point in a line to a given point
	 * 
	 * @param px
	 *            x-coordinate of the point
	 * @param py
	 *            y-coordinate of the point
	 * @param ax
	 *            x-coordinate of the beginning of the segment
	 * @param ay
	 *            y-coordinate of the beginning of the segment
	 * @param bx
	 *            x-coordinate of the end of the segment
	 * @param by
	 *            y-coordinate of the end of the segment
	 * @return {@code PVector} with the coordinates of the closest point
	 */
	public static PVector closestPoint(float ax, float ay, float bx, float by, float px, float py)
	{
		float dx = bx - ax;
		float dy = by - ay;

		// In the case of a degenerate line return first point of the segment
		if (Math.abs(dx) <= PConstants.EPSILON && Math.abs(dy) <= PConstants.EPSILON) return new PVector(ax, ay);

		// Factor to calculate the projection of the vector that connects point
		// and first extreme of the line
		float u = ((px - ax) * dx + (py - ay) * dy) / (dx * dx + dy * dy);

		// Detects the corner cases in which the closest point is one of the
		// extremes of the line segment
		if (u < 0)
			return new PVector(ax, ay);
		else if (u > 1) return new PVector(bx, by);

		// Default case
		return new PVector(ax + u * dx, ay + u * dy);
	}

	/**
	 * Returns the squared distance between a line segment and a point
	 * 
	 * @param px
	 *            x-coordinate of the point.
	 * @param py
	 *            y-coordinate of the point.
	 * @param ax
	 *            x-coordinate of the beginning of the segment.
	 * @param ay
	 *            y-coordinate of the beginning of the segment.
	 * @param bx
	 *            x-coordinate of the ending of the segment.
	 * @param by
	 *            y-coordinate of the ending of the segment.
	 * @return Value of the squared distance.
	 */
	public static float squaredDistance(float ax, float ay, float bx, float by, float px, float py)
	{
		PVector closest = closestPoint(ax, ay, bx, by, px, py);
		return ((closest.x - px) * (closest.x - px) + (closest.y - py) * (closest.y - py));
	}

	/**
	 * Returns the squared distance between a line segment and a point
	 * 
	 * @param px
	 *            x-coordinate of the point.
	 * @param py
	 *            y-coordinate of the point.
	 * @param ax
	 *            x-coordinate of the beginning of the segment.
	 * @param ay
	 *            y-coordinate of the beginning of the segment.
	 * @param bx
	 *            x-coordinate of the ending of the segment.
	 * @param by
	 *            y-coordinate of the ending of the segment.
	 * @return Value of the distance.
	 */
	public static float distance(float ax, float ay, float bx, float by, float px, float py)
	{
		return (float) Math.sqrt(squaredDistance(px, py, ax, ay, bx, by));
	}

	/**
	 * Calculates the intersection point of two line segments
	 * 
	 * @param ax
	 *            x-coordinate of the beginning of the first segment.
	 * @param ay
	 *            y-coordinate of the beginning of the first segment.
	 * @param bx
	 *            x-coordinate of the ending of the first segment.
	 * @param by
	 *            y-coordinate of the ending of the first segment.
	 * @param cx
	 *            x-coordinate of the beginning of the second segment.
	 * @param cy
	 *            y-coordinate of the beginning of the second segment.
	 * @param dx
	 *            x-coordinate of the ending of the second segment.
	 * @param dy
	 *            y-coordinate of the ending of the second segment.
	 * @return Intersection point if both lines intersect. {@code Null} in case
	 *         there's no intersection.
	 */
	public static PVector intersect(float ax, float ay, float bx, float by, float cx, float cy, float dx, float dy)
	{
		// Check if lines are parallel.
		float d = (dy - cy) * (bx - ax) - (dx - cx) * (by - ay);
		if (Math.abs(d) < PConstants.EPSILON) 
			return null;

		// Check if lines intersect.
		float na = (dx - cx) * (ay - cy) - (dy - cy) * (ax - cx);
		float nb = (bx - ax) * (ay - cy) - (by - ay) * (ax - cx);
		float ma = na / d;
		float mb = nb / d;
		if (ma < 0 || ma > 1 || mb < 0 || mb > 1) 
			return null;

		return new PVector(ax + ma * (bx - ax), ay + ma * (by - ay));
	}

	/**
	 * Calculates the minimum distance between a segment and the center of a
	 * circle
	 * 
	 * @param x1
	 *            x-coordinate of the first point of the segment.
	 * @param y1
	 *            y-coordinate of the first point of the segment.
	 * @param x2
	 *            x-coordinate of the second point of the segment.
	 * @param y2
	 *            y-coordinate of the second point of the segment.
	 * @param cx
	 *            x-coordinate of the center of the circle.
	 * @param cy
	 *            y-coordinate of the center of the circle.
	 * @param r
	 *            Radius of the circle.
	 * @return Distance between the segment and the center of the circle.
	 */
	public static float distanceToCenter(float x1, float y1, float x2, float y2, float cx, float cy, float r)
	{
		PVector closest = closestPoint(x1, y1, x2, y2, cx, cy);
		float d = (float) Math.sqrt((closest.x - cx) * (closest.x - cx) + (closest.y - cy) * (closest.y - cy));
		return d;
	}

	/**
	 * Calculates the squared minimum distance between a segment and the center
	 * of a circle
	 * 
	 * @param x1
	 *            x-coordinate of the first point of the segment.
	 * @param y1
	 *            y-coordinate of the first point of the segment.
	 * @param x2
	 *            x-coordinate of the second point of the segment.
	 * @param y2
	 *            y-coordinate of the second point of the segment.
	 * @param cx
	 *            x-coordinate of the center of the circle.
	 * @param cy
	 *            y-coordinate of the center of the circle.
	 * @param r
	 *            Radius of the circle.
	 * @return Squared distance between the segment and the center of the
	 *         circle.
	 */
	public static float squaredDistanceToCenter(float x1, float y1, float x2, float y2, float cx, float cy, float r)
	{
		PVector closest = closestPoint(x1, y1, x2, y2, cx, cy);
		float d = (closest.x - cx) * (closest.x - cx) + (closest.y - cy) * (closest.y - cy);
		return d;
	}

	/**
	 * Calculates the minimum distance between a segment and the perimeter of a
	 * circle
	 * 
	 * @param x1
	 *            x-coordinate of the first point of the segment.
	 * @param y1
	 *            y-coordinate of the first point of the segment.
	 * @param x2
	 *            x-coordinate of the second point of the segment.
	 * @param y2
	 *            y-coordinate of the second point of the segment.
	 * @param cx
	 *            x-coordinate of the center of the circle.
	 * @param cy
	 *            y-coordinate of the center of the circle.
	 * @param r
	 *            Radius of the circle.
	 * @return Distance between the segment and the perimeter of the circle.
	 */
	public static float distanceToCircle(float x1, float y1, float x2, float y2, float cx, float cy, float r)
	{
		return Math.abs(distanceToCenter(x1, y1, x2, y2, cx, cy, r) - r);
	}

	/**
	 * This method calculates the squared minimum distance between a segment and
	 * the perimeter of a circle
	 * 
	 * @param x1
	 *            x-coordinate of the first point of the segment.
	 * @param y1
	 *            y-coordinate of the first point of the segment.
	 * @param x2
	 *            x-coordinate of the second point of the segment.
	 * @param y2
	 *            y-coordinate of the second point of the segment.
	 * @param cx
	 *            x-coordinate of the center of the circle.
	 * @param cy
	 *            y-coordinate of the center of the circle.
	 * @param r
	 *            Radius of the circle.
	 * @return Squared distance between the segment and the perimeter of the
	 *         circle.
	 */
	public static float squaredDistanceToCircle(float x1, float y1, float x2, float y2, float cx, float cy, float r)
	{
		float d = distanceToCircle(x1, y1, x2, y2, cx, cy, r);
		return d * d;
	}

	/**
	 * This method calculates the intersection points between a segment and a
	 * circle
	 * 
	 * @param x1
	 *            x-coordinate of the first point of the segment.
	 * @param y1
	 *            y-coordinate of the first point of the segment.
	 * @param x2
	 *            x-coordinate of the second point of the segment.
	 * @param y2
	 *            y-coordinate of the second point of the segment.
	 * @param cx
	 *            x-coordinate of the center of the circle.
	 * @param cy
	 *            y-coordinate of the center of the circle.
	 * @param r
	 *            Radius of the circle.
	 * @return An arraylist of intersection points. {@code Null} if there is no
	 *         intersection.
	 */
	public static ArrayList<PVector> intersect(float x1, float y1, float x2, float y2, float cx, float cy, float r)
	{
		if (x2 == x1 && y2 == y1)
			return null;
		if (r <= 0) throw new IllegalArgumentException("The radius of the circle must be bigger than 0.");

		float sqR = r * r;
		// Check if segment extremes are inside the circle
		boolean oneInside = (cx - x1) * (cx - x1) + (cy - y1) * (cy - y1) < sqR;
		boolean twoInside = (cx - x2) * (cx - x2) + (cy - y2) * (cy - y2) < sqR;

		// If both extremes are inside there is no intersection
		if (oneInside && twoInside) return null;

		// Get a normalized vector (sx, sy) in the direction of the segment and
		// its magnitude (sl)
		float dx = x2 - x1;
		float dy = y2 - y1;
		float sl = (float) Math.sqrt(dx * dx + dy * dy);
		float sx = dx / sl;
		float sy = dy / sl;

		// Calculate the length of the projected line (pl) connecting the origin
		// of the segment (x1, y1) to the center of the circle (cx, cy) using
		// dot product
		float pl = (cx - x1) * sx + (cy - y1) * sy;

		// Check if closest point in the segment is one of the segment extremes
		// and if there is intersection in that case
		if ((pl < 0 && !oneInside) || (pl > sl && !twoInside)) return null;

		// Else, closest point is between both extremes
		// If distance from this point to center is bigger than radius there is
		// no intersection
		float x = x1 + sx * pl;
		float y = y1 + sy * pl;
		float d = sqR - ((x - cx) * (x - cx) + (y - cy) * (y - cy));
		if (d < 0) return null;

		// Calculate distance from this point to intersection points (applying
		// Pythagoras theorem)
		d = (float) Math.sqrt(d);
		ArrayList<PVector> points = new ArrayList<PVector>();
		// Calculate intersection points
		if (oneInside){ 
			points.add(new PVector(x + sx * d, y + sy * d));
			return points;
		}
		if (twoInside){
			points.add(new PVector(x - sx * d, y - sy * d));
			return points;
		}
		points.add(new PVector(x + sx * d, y + sy * d));
		points.add(new PVector(x - sx * d, y - sy * d));
		return points;
	}

	/**
	 * @param x1
	 *            x-coordinate of the beginning of the segment.
	 * @param y1
	 *            y-coordinate of the beginning of the segment.
	 * @param x2
	 *            x-coordinate of the ending of the segment.
	 * @param y2
	 *            y-coordinate of the ending of the segment.
	 * @param n
	 *            Number of parts to divide the segment.
	 * @return A PVector array containing the positions of the division points,
	 *         including the extreme points passed to the method.
	 */
	static PVector[] divide(float x1, float y1, float x2, float y2, int n)
	{
		if (n < 1) throw new IllegalArgumentException("The number of equal parts must be positive.");
		PVector[] p = new PVector[n + 1];
		// Calculates the components of the vector 'd' that connect consecutive
		// division points in the direction beginning->ending
		float dx = (x2 - x1) / n;
		float dy = (y2 - y1) / n;
		// Calculates the array adding consecutively 'd' to a previous point,
		// starting in the beginning of the segment
		p[0] = new PVector(x1, y1);
		for (int i = 0; i < n; i++)
			p[i + 1] = new PVector(p[i].x + dx, p[i].y + dy);
		return p;
	}
}
