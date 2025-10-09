package org.billing.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PhoneNumberTest {
    @Test
    void testPhoneNumber() {
        PhoneNumber pn = PhoneNumber.of("00375-55-1234567");
        assertEquals(pn.getCountryCode(),"00375");
        assertEquals(pn.getOperatorCode(),"55");
        assertEquals(pn.getNumber(),"00375-55-1234567");
    }
    @Test
    void testIsRoaming(){
        PhoneNumber pn = PhoneNumber.of("11111-20-1234567");
        assertTrue(pn.isRoaming(),"Should be Roaming");
        assertFalse(pn.isInNetwork(),"Should be Not Roaming");
        assertFalse(pn.isOtherMobile(),"Should be Not Other Mobile");
        assertFalse(pn.isFixedLine(),"Should be Not FixedLine");
    }
    @Test
    void testIsInNetwork(){
        PhoneNumber pn = PhoneNumber.of("00375-55-4444444");
        assertTrue(pn.isInNetwork(),"Should be In Network");
        assertFalse(pn.isRoaming(),"Should be Not In Roaming");
        assertFalse(pn.isFixedLine(),"Should be Not FixedLine");
        assertFalse(pn.isOtherMobile(),"Should be Not Other Mobile");
    }
    @Test
    void testIsOtherMobile(){
        PhoneNumber pn1 = PhoneNumber.of("00375-25-4444444");
        PhoneNumber pn2 = PhoneNumber.of("00375-29-4444444");
        PhoneNumber pn3 = PhoneNumber.of("00375-33-4444444");
        PhoneNumber pn4 = PhoneNumber.of("00375-44-4444444");
        assertTrue(pn1.isOtherMobile(),"Should be Other Mobile");
        assertTrue(pn2.isOtherMobile(),"Should be Other Mobile");
        assertTrue(pn3.isOtherMobile(),"Should be Other Mobile");
        assertTrue(pn4.isOtherMobile(),"Should be Other Mobile");
        assertFalse(pn1.isInNetwork(),"Should be Not In Network");
        assertFalse(pn2.isInNetwork(),"Should be Not In Network");
        assertFalse(pn3.isInNetwork(),"Should be Not In Network");
        assertFalse(pn4.isInNetwork(),"Should be Not In Network");
        assertFalse(pn1.isRoaming(),"Should be Not Roaming");
        assertFalse(pn2.isRoaming(),"Should be Not Roaming");
        assertFalse(pn3.isRoaming(),"Should be Not Roaming");
        assertFalse(pn4.isRoaming(),"Should be Not Roaming");
        assertFalse(pn1.isFixedLine(),"Should be Not FixedLine");
        assertFalse(pn2.isFixedLine(),"Should be Not FixedLine");
        assertFalse(pn3.isFixedLine(),"Should be Not FixedLine");
        assertFalse(pn4.isFixedLine(),"Should be Not FixedLine");
    }
    @Test
    void isFixedLine(){
        PhoneNumber pn = PhoneNumber.of("00375-00-0000000");
        assertTrue(pn.isFixedLine(),"Should be FixedLine");
        assertFalse(pn.isRoaming(),"Should be Roaming");
        assertFalse(pn.isInNetwork(),"Should be Not Roaming");
        assertFalse(pn.isOtherMobile(),"Should be Not Other Mobile");
    }
}
