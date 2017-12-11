/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.unicorncollege.bt.model;

/**
 * This Enum is for telling, how new reservation conflicts with existing one
 * For instance only initial time is overlapping, let's tell our program, we
 * need to modify only initial time, so user has to enter only one new time.
 * 
 * @author DB-47
 */
public enum ReservationConflictType {
    INITIAL, FINISH, BOTH, NONE
}
