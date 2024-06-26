package seedu.teachstack.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.teachstack.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.teachstack.model.util.SampleDataUtil.getGroupSet;
import static seedu.teachstack.testutil.Assert.assertThrows;
import static seedu.teachstack.testutil.TypicalPersons.ALICE;
import static seedu.teachstack.testutil.TypicalPersons.BENSON;
import static seedu.teachstack.testutil.TypicalPersons.GEORGE;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.teachstack.commons.core.GuiSettings;
import seedu.teachstack.model.person.PersonInGroupPredicate;
import seedu.teachstack.testutil.AddressBookBuilder;
import seedu.teachstack.testutil.ArchivedBookBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
        assertEquals(new ArchivedBook(), new ArchivedBook(modelManager.getArchivedBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasEmail_emailInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasEmail(ALICE));
    }

    @Test
    public void hasEmail_emailNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasEmail(ALICE));
    }

    @Test
    public void hasId_studentIdNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasId(ALICE));
    }

    @Test
    public void hasId_studentIdInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasId(ALICE));
    }

    @Test
    public void getWeak_personBelowThreshold_returnsPersonInList() {
        modelManager.addPerson(GEORGE);
        assertEquals(modelManager.getAddressBook().getPersonList(), modelManager.getWeak());
        modelManager.addPerson(ALICE);
        assertNotEquals(modelManager.getAddressBook().getPersonList(), modelManager.getWeak());
        assertNotEquals(modelManager.getAddressBook().getPersonList().size(), modelManager.getWeak().size());
        assertEquals(modelManager.getAddressBook().getPersonList().get(0), modelManager.getWeak().get(0));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
    }

    @Test
    public void setArchivedBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setArchivedBookFilePath(null));
    }

    @Test
    public void setArchivedBookFilePath_validPath_setsArchivedBookFilePath() {
        Path path = Paths.get("archived/book/file/path");
        modelManager.setArchivedBookFilePath(path);
        assertEquals(path, modelManager.getArchivedBookFilePath());
    }

    @Test
    public void getFilteredArchivedList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredArchivedList().remove(0));
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        ArchivedBook archivedBook = new ArchivedBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        ArchivedBook differentArchivedBook = new ArchivedBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, archivedBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, archivedBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, archivedBook, userPrefs)));

        // different archivedBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentArchivedBook, userPrefs)));

        // different filteredList -> returns false
        modelManager.updateFilteredPersonList(new PersonInGroupPredicate(getGroupSet("nooneingroup")));
        assertFalse(modelManager.equals(new ModelManager(addressBook, archivedBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, archivedBook, differentUserPrefs)));
    }
}
