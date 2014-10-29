/*
 * This file is part of MyPet
 *
 * Copyright (C) 2011-2014 Keyle
 * MyPet is licensed under the GNU Lesser General Public License.
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.keyle.mypet.listeners;

import de.keyle.mypet.api.event.MyPetLevelUpEvent;
import de.keyle.mypet.entity.types.MyPet;
import de.keyle.mypet.entity.types.MyPet.PetState;
import de.keyle.mypet.skill.Experience;
import de.keyle.mypet.skill.skills.info.ISkillInfo;
import de.keyle.mypet.skill.skilltree.SkillTree;
import de.keyle.mypet.skill.skilltree.SkillTreeLevel;
import de.keyle.mypet.util.Colorizer;
import de.keyle.mypet.util.Configuration;
import de.keyle.mypet.util.Util;
import de.keyle.mypet.util.locale.Locales;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.List;

public class LevelUpListener implements Listener {
    @EventHandler
    public void onLevelUp(MyPetLevelUpEvent event) {
        MyPet myPet = event.getPet();
        int lvl = event.getLevel();
        int lastLvl = event.getLastLevel();

        if (!event.isQuiet()) {
            int maxlevel = myPet.getSkillTree() != null ? myPet.getSkillTree().getMaxLevel() : 0;
            if (maxlevel != 0 && lvl >= maxlevel) {
                myPet.sendMessageToOwner(Util.formatText(Locales.getString("Message.LevelSystem.ReachedMaxLevel", event.getOwner().getLanguage()), myPet.getPetName(), maxlevel));
            } else {
                myPet.sendMessageToOwner(Util.formatText(Locales.getString("Message.LevelSystem.LevelUp", event.getOwner().getLanguage()), myPet.getPetName(), event.getLevel()));
            }
        }
        SkillTree skillTree = myPet.getSkillTree();
        if (skillTree != null) {
            if (skillTree.getLastLevelWithSkills() < lvl) {
                lvl = skillTree.getLastLevelWithSkills();
            }
            for (int i = lastLvl + 1; i <= lvl; i++) {
                if (skillTree.hasLevel(i)) {
                    SkillTreeLevel level = skillTree.getLevel(i);
                    if (!event.isQuiet()) {
                        if (level.hasLevelupMessage()) {
                            myPet.sendMessageToOwner(Colorizer.setColors(level.getLevelupMessage()));
                        }
                    }

                    List<ISkillInfo> skillList = level.getSkills();
                    for (ISkillInfo skill : skillList) {
                        myPet.getSkills().getSkill(skill.getName()).upgrade(skill, event.isQuiet());
                    }
                }
            }
        }

        if (myPet.getStatus() == PetState.Here) {
            myPet.getCraftPet().getHandle().setCustomName("");
            if (!event.isQuiet()) {
                myPet.setHealth(myPet.getMaxHealth());
                myPet.setHungerValue(100);

                if (Experience.FIREWORK_ON_LEVELUP) {
                    Firework fw = (Firework) myPet.getLocation().getWorld().spawnEntity(myPet.getLocation(), EntityType.FIREWORK);
                    FireworkEffect fwe = FireworkEffect.builder().with(Type.STAR).withColor(Color.fromRGB(Configuration.LEVELUP_FIREWORK_COLOR)).withTrail().withFlicker().build();
                    FireworkMeta fwm = fw.getFireworkMeta();
                    fwm.addEffect(fwe);
                    fwm.addEffect(fwe);
                    fwm.addEffect(fwe);
                    fwm.setPower(0);
                    fw.setFireworkMeta(fwm);
                    fw.detonate();
                }
            }
        }
    }
}