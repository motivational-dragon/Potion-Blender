package mod.motivationaldragon.potionblender.config;

import com.google.gson.*;
import mod.motivationaldragon.potionblender.Constants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.lang.reflect.Type;
public class ItemSerializer implements JsonSerializer<Item>, JsonDeserializer<Item> {
		@Override
		public JsonElement serialize(Item item, Type type, JsonSerializationContext jsonSerializationContext) {
			String itemName = BuiltInRegistries.ITEM.getKey(item).toString();
			return new JsonPrimitive(itemName);
		}

		@Override
		public Item deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
			String itemName = jsonElement.getAsString();
			Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(itemName));
			if(item == Items.AIR){
				Constants.LOG.warn(String.format("Item %s was parsed as minecraft air! Are you sure item name is valid?", itemName));
			}
			return item;
		}
}
