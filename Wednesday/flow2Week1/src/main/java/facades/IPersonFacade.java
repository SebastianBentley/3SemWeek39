package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import exceptions.MissingInput;
import exceptions.PersonNotFound;

public interface IPersonFacade {

    public PersonDTO addPerson(PersonDTO p) throws MissingInput;

    public PersonDTO deletePerson(Long id) throws PersonNotFound;

    public PersonDTO getPerson(Long id) throws PersonNotFound;

    public PersonsDTO getAllPersons();

    public PersonDTO editPerson(PersonDTO p) throws PersonNotFound, MissingInput;

}
