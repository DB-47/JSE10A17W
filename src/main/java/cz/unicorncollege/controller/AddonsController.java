package cz.unicorncollege.controller;

import java.util.ArrayList;
import java.util.List;

import cz.unicorncollege.bt.utils.Choices;

public class AddonsController {

	public void showAddonsMenu() {
		List<String> choices = new ArrayList<String>();
		choices.add("List all Addons");
		choices.add("Add new Addon");
		choices.add("Accept delivery");
		choices.add("Hand Over");

		while (true) {
			switch (Choices.getChoice("Choose what do you want to do: ", choices)) {
			case 1:
				listAllAddons();
				break;
			case 2:
				addNewAddon();
				break;
			case 3:
				acceptDelivery();
				break;
			case 4:
				handOver();
				break;
			case 5:
				return;
			}
		}
		
	}
	
	private void listAllAddons() {
		
	}
	
	private void addNewAddon() {
		
	}
	
	private void acceptDelivery() {
		
	}
	
	private void handOver() {
		
	}
}
