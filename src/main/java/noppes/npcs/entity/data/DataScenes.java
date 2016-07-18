//

//

package noppes.npcs.entity.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.controllers.Line;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.EntityProjectile;

public class DataScenes {
	public class SceneContainer {
		public int btn;
		public String name;
		public String lines;
		public boolean enabled;
		public int ticks;
		private SceneState state;
		private List<SceneEvent> events;

		public SceneContainer() {
			btn = 0;
			name = "";
			lines = "";
			enabled = false;
			ticks = -1;
			state = null;
			events = new ArrayList<SceneEvent>();
		}

		private void handle(final SceneEvent event) throws Exception {
			if (event.type == SceneType.MOVE) {
				String[] param = event.param.split(" ");
				while (param.length > 1) {
					boolean move = false;
					if (param[0].startsWith("to")) {
						move = true;
					} else if (!param[0].startsWith("tp")) {
						break;
					}
					BlockPos pos = null;
					if (param[0].startsWith("@")) {
						final EntityLivingBase entitylivingbase = (EntityLivingBase) CommandBase
								.getEntity((ICommandSender) npc, param[0], (Class) EntityLivingBase.class);
						if (entitylivingbase != null) {
							pos = entitylivingbase.getPosition();
						}
						param = Arrays.copyOfRange(param, 2, param.length);
					} else {
						if (param.length < 4) {
							return;
						}
						pos = CommandBase.parseBlockPos(npc, param, 1, false);
						param = Arrays.copyOfRange(param, 4, param.length);
					}
					if (pos == null) {
						continue;
					}
					npc.ai.setStartPos(pos);
					npc.getNavigator().clearPathEntity();
					if (move) {
						final PathEntity pathentity = npc.getNavigator().getPathToPos(pos);
						npc.getNavigator().setPath(pathentity, 1.0);
					} else {
						if (npc.isInRange(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 2.0)) {
							continue;
						}
						npc.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
					}
				}
			} else if (event.type == SceneType.SAY) {
				npc.saySurrounding(new Line(event.param));
			} else if (event.type == SceneType.ROTATE) {
				npc.lookAi.resetTask();
				if (event.param.startsWith("@")) {
					final EntityLivingBase entitylivingbase2 = (EntityLivingBase) CommandBase
							.getEntity((ICommandSender) npc, event.param, (Class) EntityLivingBase.class);
					npc.lookAi.rotate(npc.worldObj.getClosestPlayerToEntity(entitylivingbase2, 30.0));
				} else {
					npc.lookAi.rotate(Integer.parseInt(event.param));
				}
			} else if (event.type == SceneType.EQUIP) {
				final String[] args = event.param.split(" ");
				if (args.length < 2) {
					return;
				}
				IItemStack itemstack = null;
				if (!args[1].equalsIgnoreCase("none")) {
					final Item item = CommandBase.getItemByText(npc, args[1]);
					final int i = (args.length >= 3) ? CommandBase.parseInt(args[2], 1, 64) : 1;
					final int j = (args.length >= 4) ? CommandBase.parseInt(args[3]) : 0;
					itemstack = new ItemStackWrapper(new ItemStack(item, i, j));
				}
				if (args[0].equalsIgnoreCase("main")) {
					npc.inventory.weapons.put(0, itemstack);
				} else if (args[0].equalsIgnoreCase("off")) {
					npc.inventory.weapons.put(2, itemstack);
				} else if (args[0].equalsIgnoreCase("proj")) {
					npc.inventory.weapons.put(1, itemstack);
				} else if (args[0].equalsIgnoreCase("head")) {
					npc.inventory.armor.put(0, itemstack);
				} else if (args[0].equalsIgnoreCase("body")) {
					npc.inventory.armor.put(1, itemstack);
				} else if (args[0].equalsIgnoreCase("legs")) {
					npc.inventory.armor.put(2, itemstack);
				} else if (args[0].equalsIgnoreCase("boots")) {
					npc.inventory.armor.put(3, itemstack);
				}
			} else if (event.type == SceneType.ATTACK) {
				if (event.param.equals("none")) {
					npc.setAttackTarget(null);
				} else {
					final EntityLivingBase entity = (EntityLivingBase) CommandBase.getEntity((ICommandSender) npc,
							event.param, (Class) EntityLivingBase.class);
					if (entity != null) {
						npc.setAttackTarget(entity);
					}
				}
			} else if (event.type == SceneType.THROW) {
				final String[] args = event.param.split(" ");
				final EntityLivingBase entity2 = (EntityLivingBase) CommandBase.getEntity((ICommandSender) npc, args[0],
						(Class) EntityLivingBase.class);
				if (entity2 == null) {
					return;
				}
				float damage = Float.parseFloat(args[1]);
				if (damage <= 0.0f) {
					damage = 0.01f;
				}
				ItemStack stack = ItemStackWrapper.MCItem(npc.inventory.getProjectile());
				if (args.length > 2) {
					final Item item2 = CommandBase.getItemByText(npc, args[2]);
					stack = new ItemStack(item2, 1, 0);
				}
				final EntityProjectile projectile = npc.shoot(entity2, 100, stack, false);
				projectile.damage = damage;
			} else if (event.type == SceneType.ANIMATE) {
				npc.animateAi.temp = 0;
				if (event.param.equalsIgnoreCase("sleep")) {
					npc.animateAi.temp = 2;
				} else if (event.param.equalsIgnoreCase("sneak")) {
					npc.ai.animationType = 4;
				} else if (event.param.equalsIgnoreCase("normal")) {
					npc.ai.animationType = 0;
				} else if (event.param.equalsIgnoreCase("sit")) {
					npc.animateAi.temp = 1;
				} else if (event.param.equalsIgnoreCase("crawl")) {
					npc.ai.animationType = 7;
				} else if (event.param.equalsIgnoreCase("bow")) {
					npc.animateAi.temp = 11;
				} else if (event.param.equalsIgnoreCase("yes")) {
					npc.animateAi.temp = 13;
				} else if (event.param.equalsIgnoreCase("no")) {
					npc.animateAi.temp = 12;
				}
			} else if (event.type == SceneType.COMMAND) {
				NoppesUtilServer.runCommand(npc, npc.getName(), event.param, null);
			} else if (event.type == SceneType.FACTION) {
				npc.setFaction(Integer.parseInt(event.param));
			} else if (event.type == SceneType.FOLLOW) {
				if (event.param.equalsIgnoreCase("none")) {
					owner = null;
					ownerScene = null;
				} else {
					final EntityLivingBase entity = (EntityLivingBase) CommandBase.getEntity((ICommandSender) npc,
							event.param, (Class) EntityLivingBase.class);
					if (entity == null) {
						return;
					}
					owner = entity;
					ownerScene = name;
				}
			}
		}

		public void readFromNBT(final NBTTagCompound compound) {
			enabled = compound.getBoolean("Enabled");
			name = compound.getString("Name");
			lines = compound.getString("Lines");
			btn = compound.getInteger("Button");
			ticks = compound.getInteger("Ticks");
			events = new ArrayList<SceneEvent>();
			for (final String line : lines.split("\r\n|\r|\n")) {
				final SceneEvent event = SceneEvent.parse(line);
				if (event != null) {
					events.add(event);
				}
			}
			Collections.sort(events);
		}

		public void update() {
			if (!enabled || events.isEmpty() || (state == null)) {
				return;
			}
			for (final SceneEvent event : events) {
				if (event.ticks > state.ticks) {
					break;
				}
				if (event.ticks != state.ticks) {
					continue;
				}
				try {
					handle(event);
				} catch (Exception ex) {
				}
			}
			ticks = state.ticks;
		}

		public boolean validState() {
			if (!enabled) {
				return false;
			}
			if (state != null) {
				if (DataScenes.StartedScenes.containsValue(state)) {
					return !state.paused;
				}
				state = null;
			}
			state = DataScenes.StartedScenes.get(name.toLowerCase());
			if (state == null) {
				state = DataScenes.StartedScenes.get(btn + "btn");
			}
			return (state != null) && !state.paused;
		}

		public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
			compound.setBoolean("Enabled", enabled);
			compound.setString("Name", name);
			compound.setString("Lines", lines);
			compound.setInteger("Button", btn);
			compound.setInteger("Ticks", ticks);
			return compound;
		}
	}

	public static class SceneEvent implements Comparable<SceneEvent> {
		public static SceneEvent parse(String str) {
			final SceneEvent event = new SceneEvent();
			int i = str.indexOf(" ");
			if (i <= 0) {
				return null;
			}
			try {
				event.ticks = Integer.parseInt(str.substring(0, i));
				str = str.substring(i + 1);
			} catch (NumberFormatException ex) {
				return null;
			}
			i = str.indexOf(" ");
			if (i <= 0) {
				return null;
			}
			final String name = str.substring(0, i);
			for (final SceneType type : SceneType.values()) {
				if (name.equalsIgnoreCase(type.name())) {
					event.type = type;
				}
			}
			if (event.type == null) {
				return null;
			}
			event.param = str.substring(i + 1);
			return event;
		}

		public int ticks;
		public SceneType type;

		public String param;

		public SceneEvent() {
			ticks = 0;
			param = "";
		}

		@Override
		public int compareTo(final SceneEvent o) {
			return ticks - o.ticks;
		}

		@Override
		public String toString() {
			return ticks + " " + type.name() + " " + param;
		}
	}

	public static class SceneState {
		public boolean paused;
		public int ticks;

		public SceneState() {
			paused = false;
			ticks = -1;
		}
	}

	public enum SceneType {
		ANIMATE, MOVE, FACTION, COMMAND, EQUIP, THROW, ATTACK, FOLLOW, SAY, ROTATE;
	}

	public static Map<String, SceneState> StartedScenes;
	public static List<SceneContainer> ScenesToRun;

	static {
		DataScenes.StartedScenes = new HashMap<String, SceneState>();
		DataScenes.ScenesToRun = new ArrayList<SceneContainer>();
	}

	public static void Pause(final ICommandSender sender, final String id) {
		if (id == null) {
			for (final SceneState state : DataScenes.StartedScenes.values()) {
				state.paused = true;
			}
			NoppesUtilServer.NotifyOPs("Paused all scenes", new Object[0]);
		} else {
			final SceneState state2 = DataScenes.StartedScenes.get(id.toLowerCase());
			state2.paused = true;
			NoppesUtilServer.NotifyOPs("Paused scene %s at %s", id, state2.ticks);
		}
	}

	public static void Reset(final ICommandSender sender, final String id) {
		if (id == null) {
			DataScenes.StartedScenes = new HashMap<String, SceneState>();
			NoppesUtilServer.NotifyOPs("Reset all scene", new Object[0]);
		} else if (DataScenes.StartedScenes.remove(id.toLowerCase()) == null) {
			sender.addChatMessage(new ChatComponentTranslation("Unknown scene %s ", new Object[] { id }));
		} else {
			NoppesUtilServer.NotifyOPs("Reset scene %s", id);
		}
	}

	public static void Start(final ICommandSender sender, final String id) {
		final SceneState state = DataScenes.StartedScenes.get(id.toLowerCase());
		if (state == null) {
			NoppesUtilServer.NotifyOPs("Started scene %s", id);
			DataScenes.StartedScenes.put(id.toLowerCase(), new SceneState());
		} else if (state.paused) {
			state.paused = false;
			NoppesUtilServer.NotifyOPs("Started scene %s from %s", id, state.ticks);
		}
	}

	public static void Toggle(final ICommandSender sender, final String id) {
		final SceneState state = DataScenes.StartedScenes.get(id.toLowerCase());
		if ((state == null) || state.paused) {
			Start(sender, id);
		} else {
			state.paused = true;
			NoppesUtilServer.NotifyOPs("Paused scene %s at %s", id, state.ticks);
		}
	}

	private EntityNPCInterface npc;

	public List<SceneContainer> scenes;

	private EntityLivingBase owner;

	private String ownerScene;

	public DataScenes(final EntityNPCInterface npc) {
		scenes = new ArrayList<SceneContainer>();
		owner = null;
		ownerScene = null;
		this.npc = npc;
	}

	public void addScene(final String name) {
		if (name.isEmpty()) {
			return;
		}
		final SceneContainer scene = new SceneContainer();
		scene.name = name;
		scenes.add(scene);
	}

	public EntityLivingBase getOwner() {
		return owner;
	}

	public void readFromNBT(final NBTTagCompound compound) {
		final NBTTagList list = compound.getTagList("Scenes", 10);
		final List<SceneContainer> scenes = new ArrayList<SceneContainer>();
		for (int i = 0; i < list.tagCount(); ++i) {
			final SceneContainer scene = new SceneContainer();
			scene.readFromNBT(list.getCompoundTagAt(i));
			scenes.add(scene);
		}
		this.scenes = scenes;
	}

	public void update() {
		for (final SceneContainer scene : scenes) {
			if (scene.validState()) {
				DataScenes.ScenesToRun.add(scene);
			}
		}
		if ((owner != null) && !DataScenes.StartedScenes.containsKey(ownerScene.toLowerCase())) {
			owner = null;
			ownerScene = null;
		}
	}

	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		final NBTTagList list = new NBTTagList();
		for (final SceneContainer scene : scenes) {
			list.appendTag(scene.writeToNBT(new NBTTagCompound()));
		}
		compound.setTag("Scenes", list);
		return compound;
	}
}
