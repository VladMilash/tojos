/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2024 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.yegor256.tojos;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;

/**
 * Thread-safe version of {@link Tojos}.
 *
 * <p>The class is thread-safe.</p>
 *
 * @since 0.3.0
 */
public final class TjSynchronized implements Tojos {

    /**
     * The wrapped {@link Tojos}.
     */
    private final Tojos origin;

    /**
     * The read-write lock.
     */
    private final ReadWriteLock lock;

    /**
     * Ctor.
     *
     * @param tojos The tojos
     */
    public TjSynchronized(final Tojos tojos) {
        this.origin = tojos;
        this.lock = new ReentrantReadWriteLock();
    }

    @Override
    public String toString() {
        return this.origin.toString();
    }

    @Override
    public Tojo add(final String name) {
        this.lock.writeLock().lock();
        try {
            return this.origin.add(name);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public List<Tojo> select(final Predicate<Tojo> filter) {
        this.lock.readLock().lock();
        try {
            return this.origin.select(filter);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public void close() throws IOException {
        this.origin.close();
    }
}
