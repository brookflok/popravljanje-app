package com.damir.popravljanje;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.damir.popravljanje");

        noClasses()
            .that()
            .resideInAnyPackage("com.damir.popravljanje.service..")
            .or()
            .resideInAnyPackage("com.damir.popravljanje.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.damir.popravljanje.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
