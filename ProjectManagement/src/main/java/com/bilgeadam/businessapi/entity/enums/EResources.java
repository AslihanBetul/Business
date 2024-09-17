package com.bilgeadam.businessapi.entity.enums;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public enum EResources implements List<EResources> {

    EQUİPMENT1,
    EQUİPMENT2,
    EQUİPMENT3,
    EQUİPMENT4,
    EQUİPMENT5,
    EQUİPMENT6;


    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<EResources> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(EResources resources) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends EResources> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends EResources> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public EResources get(int index) {
        return null;
    }

    @Override
    public EResources set(int index, EResources element) {
        return null;
    }

    @Override
    public void add(int index, EResources element) {

    }

    @Override
    public EResources remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<EResources> listIterator() {
        return null;
    }

    @Override
    public ListIterator<EResources> listIterator(int index) {
        return null;
    }

    @Override
    public List<EResources> subList(int fromIndex, int toIndex) {
        return List.of();
    }
}
