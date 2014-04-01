package datastructures;

import java.util.List;

import main.Helper;

public interface Node {
	public String show(Helper h);
	public void report(String indent, List<String> categoryNames, List<String> attributeNames);
}
