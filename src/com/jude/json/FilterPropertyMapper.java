package com.jude.json;

public abstract class FilterPropertyMapper<T> implements PropertyMapper<T> {
	public void map(JSONObject node, T t) {
	}

	public String[] include() {
		return null;
	}

	public String[] exclude() {
		return null;
	}

	@SuppressWarnings("unchecked")
	public PropertyFilter<T> getFilter() {
		return new PropertyFilter<T>() {
			public boolean apply(T t, String name, Object value) {
				String[] exclude = FilterPropertyMapper.this.exclude();

				if (exclude != null) {
					for (String i : exclude) {
						if (i == null) {
							continue;
						}
						if (i.equals(name)) {
							return true;
						}
					}
					return false;
				}

				String[] include = FilterPropertyMapper.this.include();
				if (include != null) {
					for (String i : include) {
						if (i == null) {
							continue;
						}
						if (i.equals(name)) {
							return false;
						}
					}
					return true;
				}

				return false;
			}
		};
	}
}