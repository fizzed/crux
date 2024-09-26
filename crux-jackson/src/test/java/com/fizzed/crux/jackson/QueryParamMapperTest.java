package com.fizzed.crux.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.Test;

import java.util.*;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class QueryParamMapperTest {

    public enum Animal {
        DOG,
        CAT,
        SHEEP
    }

    static public class A {

        private Integer id;
        private String name;
        private List<String> types;
        private List<Long> limits;
        private Set<Animal> animals;

        public Integer getId() {
            return id;
        }

        public A setId(Integer id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public A setName(String name) {
            this.name = name;
            return this;
        }

        public List<String> getTypes() {
            return types;
        }

        public A setTypes(List<String> types) {
            this.types = types;
            return this;
        }

        public List<Long> getLimits() {
            return limits;
        }

        public A setLimits(List<Long> limits) {
            this.limits = limits;
            return this;
        }

        public Set<Animal> getAnimals() {
            return animals;
        }

        public A setAnimals(Set<Animal> animals) {
            this.animals = animals;
            return this;
        }
    }

    @Test
    public void toQueryParams() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();

        A a = new A();
        a.setId(1);
        a.setName("Wow");
        a.setTypes(asList("A", "B", "C"));
        a.setLimits(asList(1L, 2L, 3L));
        a.setAnimals(new LinkedHashSet<>(asList(Animal.DOG, Animal.CAT)));

        Map<String,String> values = QueryParamMapper.toQueryParams(objectMapper, a);

        assertThat(values, hasEntry("id", "1"));
        assertThat(values, hasEntry("name", "Wow"));
        assertThat(values, hasEntry("types", "A,B,C"));
        assertThat(values, hasEntry("limits", "1,2,3"));
        assertThat(values, hasEntry("animals", "DOG,CAT"));
    }

    @Test
    public void fromQueryParams() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();

        Map<String,String> values = new HashMap<>();
        values.put("id", "1");

        final A a1 = QueryParamMapper.fromQueryParams(objectMapper, values, A.class);

        assertThat(a1.getId(), is(1));

        values.put("name", "Hello!");

        final A a2 = QueryParamMapper.fromQueryParams(objectMapper, values, A.class);

        assertThat(a2.getId(), is(1));
        assertThat(a2.getName(), is("Hello!"));

        values.put("types", "C,D,F");

        final A a3 = QueryParamMapper.fromQueryParams(objectMapper, values, A.class);

        assertThat(a3.getId(), is(1));
        assertThat(a3.getName(), is("Hello!"));
        assertThat(a3.getTypes(), contains("C", "D", "F"));

        values.put("animals", "SHEEP,DOG");

        final A a4 = QueryParamMapper.fromQueryParams(objectMapper, values, A.class);

        assertThat(a4.getId(), is(1));
        assertThat(a4.getName(), is("Hello!"));
        assertThat(a4.getTypes(), contains("C", "D", "F"));
        assertThat(a4.getAnimals(), containsInAnyOrder(Animal.SHEEP, Animal.DOG));

        values.put("animals", "DOG");

        final A a5 = QueryParamMapper.fromQueryParams(objectMapper, values, A.class);

        assertThat(a5.getId(), is(1));
        assertThat(a5.getName(), is("Hello!"));
        assertThat(a5.getTypes(), contains("C", "D", "F"));
        assertThat(a5.getAnimals(), containsInAnyOrder(Animal.DOG));

        // ignore an unknown property?
        values.put("doesnot", "exist");

        final A a6 = QueryParamMapper.fromQueryParams(objectMapper, values, A.class);

        assertThat(a6.getId(), is(1));
        assertThat(a6.getName(), is("Hello!"));
        assertThat(a6.getTypes(), contains("C", "D", "F"));
        assertThat(a6.getAnimals(), containsInAnyOrder(Animal.DOG));


        // bad enum value?
        values.put("animals", "NOTFOUND");

        try {
            QueryParamMapper.fromQueryParams(objectMapper, values, A.class);
        } catch (InvalidFormatException e) {
            // expected
        }
    }

}