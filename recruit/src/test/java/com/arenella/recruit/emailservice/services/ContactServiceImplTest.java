package com.arenella.recruit.emailservice.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.emailservice.beans.Contact;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.dao.ContactDao;

/**
* Unit tests for the ContactServiceImpl class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class ContactServiceImplTest {

	@InjectMocks
	private ContactServiceImpl 	service 		= new ContactServiceImpl();
	
	@Mock
	private ContactDao 			mockContactDao;
	
	/**
	* Test fetching of Contacts 
	* @throws Exception
	*/
	@Test
	public void testFetchContacts() throws Exception{
		
		Contact contact = new Contact("rec33", ContactType.RECRUITER, "Kevin", "Parkings", "kparkings@gmail.com");
		Mockito.when(this.mockContactDao.fetchContacts(Mockito.anySet())).thenReturn(Set.of(contact));
	
		Set<Contact> contacts = service.fetchContacts(Set.of("rec11"));
		
		assertTrue(contacts.contains(contact));
		
	}
	
}
