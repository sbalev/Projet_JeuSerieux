/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modele.outils;

import java.awt.geom.Point2D;

/**
 *
 * @author brokep
 */
public class PointPathFinding implements Comparable<PointPathFinding>{
    private Point2D _point;
    private int _cout;
    private double _distance;
    public PointPathFinding(Point2D p, int cout) {
        _point = p;
        _cout = cout;
    }

    public void setDistance(double _distance) {
        this._distance = _distance;
    }

    public double getDistance() {
        return _distance;
    }

    /**
     * @return the _point
     */
    public Point2D getPoint() {
        return _point;
    }

    /**
     * @param _point the _point to set
     */
    public void setPoint(Point2D _point) {
        this._point = _point;
    }

    /**
     * @return the _cout
     */
    public int getCout() {
        return _cout;
    }

    /**
     * @param _cout the _cout to set
     */
    public void setCout(int _cout) {
        this._cout = _cout;
    }
    
    public boolean estVoisin(PointPathFinding p) {
        double diffX = _point.getX() - p._point.getX(); //différence des axes X
        double diffY = _point.getY() - p._point.getY(); //différence des axes Y
        if(Math.abs(diffY)+Math.abs(diffX)==1.0) {
            return true;
        }
        return false;
    }
    
    @Override
    public int compareTo(PointPathFinding p) {
        if(_cout!=p._cout) {
            if(this._distance>p._distance) {
                return 1;
            } else {
                return -1;
            }
        } else {
            if(this._cout>p._cout) {
                return 1;
            } else {
                return -1;
            }
        }
            
    }
}