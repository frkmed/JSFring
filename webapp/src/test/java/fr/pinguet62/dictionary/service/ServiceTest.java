package fr.pinguet62.dictionary.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.Serializable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import fr.pinguet62.dictionary.model.Language;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@DatabaseSetup("/dataset.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
public final class ServiceTest {

    @Autowired
    private LanguageService languageService;

    /** Test for {@link AbstractService#create(Object)}. */
    @Test
    public void test_create() {
        assertEquals(3, languageService.count());
        languageService.create(new Language("es", "Espa�ol"));
        assertEquals(4, languageService.count());
    }

    /** Test for {@link AbstractService#get(Serializable)}. */
    @Test
    public void test_get() {
        assertEquals("Fran�ais", languageService.get("fr").getName());
        assertNull(languageService.get("  "));
    }

}