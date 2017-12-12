/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.unicorncollege.bt.model;

/**
 *
 * @author DB-47-PG
 */
public enum CRUDOperation {
    UPDATE, DELETE, CREATE, READ;
    
    @Override    
    public String toString() {
        return name().toLowerCase();
    }
}
