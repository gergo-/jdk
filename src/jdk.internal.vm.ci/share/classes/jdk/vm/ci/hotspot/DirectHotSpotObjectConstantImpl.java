/*
 * Copyright (c) 2018, 2024, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package jdk.vm.ci.hotspot;

import jdk.vm.ci.meta.JavaConstant;

final class DirectHotSpotObjectConstantImpl extends HotSpotObjectConstantImpl {

    static JavaConstant forObject(Object object, boolean compressed) {
        if (object == null) {
            return compressed ? HotSpotCompressedNullConstant.COMPRESSED_NULL : JavaConstant.NULL_POINTER;
        } else {
            return new DirectHotSpotObjectConstantImpl(object, compressed);
        }
    }

    static HotSpotObjectConstantImpl forNonNullObject(Object object, boolean compressed) {
        if (object == null) {
            throw new NullPointerException();
        }
        return new DirectHotSpotObjectConstantImpl(object, compressed);
    }

    private DirectHotSpotObjectConstantImpl(Object object, boolean compressed) {
        super(compressed);
        assert object != null;
        this.object = object;
    }

    final Object object;

    @Override
    public JavaConstant compress() {
        if (compressed) {
            throw new IllegalArgumentException("already compressed: " + this);
        }
        return new DirectHotSpotObjectConstantImpl(object, true);
    }

    @Override
    public JavaConstant uncompress() {
        if (!compressed) {
            throw new IllegalArgumentException("not compressed: " + this);
        }
        return new DirectHotSpotObjectConstantImpl(object, false);
    }

    @Override
    public int getIdentityHashCode() {
        return System.identityHashCode(object);
    }
}
