package com.fugitives.scraping;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class FugitiveTest {

    @Test
    public void testHashID(){
        Fugitive fugitive = Mockito.mock(Fugitive.class);
        Mockito.when(fugitive.getHashID()).thenReturn(0);
        assertEquals(0, fugitive.getHashID());
    }

    @Test
    public void testNullName(){
        assertThrows(
                NullPointerException.class,
                ()-> new Fugitive(null, "surname", "birthPlace", "birthDate", "organization", "color", "b64Image")
        );
    }

    @Test
    public void testHashCodeEquality(){
        Fugitive fugitive1 = new Fugitive("name", "surname", "birthPlace", "birthDate", "organization", "color", "b64Image");
        Fugitive fugitive2 = new Fugitive("name", "surname", "birthPlace", "birthDate", "organization", "color", "b64Image");
        assertEquals(fugitive1.hashCode(), fugitive2.hashCode());
    }
}