package cz.unicorncollege.bt.model;

public class Addon {
	private long id;
	private Category category;	
	private String name;
	private int amount;
	private int minimalAmount;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public int getMinimalAmount() {
		return minimalAmount;
	}
	
	public void setMinimalAmount(int minimalAmount) {
		this.minimalAmount = minimalAmount;
	}
}
