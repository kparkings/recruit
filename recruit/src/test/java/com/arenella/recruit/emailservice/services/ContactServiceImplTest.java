package com.arenella.recruit.emailservice.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.emailservice.beans.Contact;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.dao.ContactDao;
import com.arenella.recruit.emailservice.entities.ContactEntityPK;

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
		
		Contact contact = new Contact("rec33", ContactType.RECRUITER, "Kevin", "Parkings", "admin@arenella-ict.com");
		Mockito.when(this.mockContactDao.fetchContacts(Mockito.anySet())).thenReturn(Set.of(contact));
	
		Set<Contact> contacts = service.fetchContacts(Set.of("rec11"));
		
		assertTrue(contacts.contains(contact));
		
	}
	
	/**
	* Tests deletion of Contact
	* @throws Exception
	*/
	@Test
	public void testDeleteContact() throws Exception{
		
		final String contactId = "1234";
		
		ArgumentCaptor<ContactEntityPK> argPK = ArgumentCaptor.forClass(ContactEntityPK.class);
		
		Mockito.when(this.mockContactDao.existsById(Mockito.any())).thenReturn(true);
		Mockito.doNothing().when(this.mockContactDao).deleteById(argPK.capture());
		
		this.service.deleteContact(ContactType.CANDIDATE, contactId);
		
		Mockito.verify(this.mockContactDao).deleteById(Mockito.any());
		
		assertEquals(ContactType.CANDIDATE, argPK.getValue().getContactType());
		assertEquals(contactId, argPK.getValue().getContactId());
		
	}
	
	/**
	* Tests deletion of Contact where no contact exists
	* @throws Exception
	*/
	@Test
	public void testDeleteContact_doesntExist() throws Exception{
		
		final String contactId = "1234";
		
		Mockito.when(this.mockContactDao.existsById(Mockito.any())).thenReturn(false);
		
		this.service.deleteContact(ContactType.CANDIDATE, contactId);
		
		Mockito.verify(this.mockContactDao, Mockito.never()).deleteById(Mockito.any());
		
	}
	
}