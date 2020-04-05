package bernie.software.utils;

import bernie.software.registry.DeepWatersEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;

public class EntityUtils
{
	public static <T extends Entity> RegistryObject<EntityType<T>> BuildWaterEntity(EntityType.IFactory<T> entity, Class<T> entityClass, float width, float height)
	{
	    String name = entityClass.getSimpleName().toLowerCase();
	    return DeepWatersEntities.ENTITIES.register(name, () -> EntityType.Builder.create(entity, EntityClassification.WATER_CREATURE)
	            .size(width, height).build(name));
	}
}