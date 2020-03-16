package model.dao;

import java.util.List;

public interface Dao {
	void insert(Object obj);
	void update(Object obj);
	void deleteById(Object id);
	Object findById(Object id);
	List<Object> findAll();
}