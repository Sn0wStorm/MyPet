/*
 * Copyright (C) 2011-2012 Keyle
 *
 * This file is part of MyPet
 *
 * MyPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MyPet. If not, see <http://www.gnu.org/licenses/>.
 */

package de.Keyle.MyPet.entity.types.zombie;

import de.Keyle.MyPet.entity.types.CraftMyPet;
import org.bukkit.craftbukkit.CraftServer;

public class CraftMyZombie extends CraftMyPet
{
    public CraftMyZombie(CraftServer server, EntityMyZombie entityMyZombie)
    {
        super(server, entityMyZombie);
    }

    @Override
    public String toString()
    {
        return "CraftMyZombie{isPet=" + getHandle().isMyPet() + ",owner=" + getOwner() + "}";
    }
}