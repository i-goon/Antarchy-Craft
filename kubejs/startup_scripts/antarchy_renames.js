// Antarchy 2.0 renamed five mobs and their drops without registry aliases.
// Alias the old ids so existing worlds keep their items and mobs.
const ResourceLocation = Java.loadClass('net.minecraft.resources.ResourceLocation')
const BuiltInRegistries = Java.loadClass('net.minecraft.core.registries.BuiltInRegistries')

const alias = (registry, from, to) => {
  try {
    registry.addAlias(ResourceLocation.parse(`antarchy:${from}`), ResourceLocation.parse(`antarchy:${to}`))
  } catch (e) {
    console.warn(`Antarchy alias ${from} -> ${to} skipped: ${e}`)
  }
}

const ENTITIES = {
  cloud_shark: 'stratoshark',
  jumpy_bug: 'springbug',
  triffid: 'flytrap',
  creeping_horror: 'crawling_blight',
  lurking_terror: 'skulking_fright'
}

const ITEMS = {
  cloud_shark_fin: 'stratoshark_fin',
  cloud_shark_fin_soup: 'stratoshark_fin_soup',
  jumpy_bug_leg: 'springbug_leg',
  jumpy_boots: 'springy_boots',
  triffid_goo: 'flytrap_goo',
  triffid_goo_block: 'flytrap_goo_block',
  cloud_shark_spawn_egg: 'stratoshark_spawn_egg',
  jumpy_bug_spawn_egg: 'springbug_spawn_egg',
  triffid_spawn_egg: 'flytrap_spawn_egg',
  creeping_horror_spawn_egg: 'crawling_blight_spawn_egg',
  lurking_terror_spawn_egg: 'skulking_fright_spawn_egg',
  creeping_horror_egg: 'crawling_blight_egg',
  jumpy_bug_egg: 'springbug_egg',
  lurking_terror_egg: 'skulking_fright_egg'
}

const BLOCKS = {
  triffid_goo_block: 'flytrap_goo_block',
  creeping_horror_egg: 'crawling_blight_egg',
  jumpy_bug_egg: 'springbug_egg',
  lurking_terror_egg: 'skulking_fright_egg'
}

Object.entries(ENTITIES).forEach(([from, to]) => alias(BuiltInRegistries.ENTITY_TYPE, from, to))
Object.entries(ITEMS).forEach(([from, to]) => alias(BuiltInRegistries.ITEM, from, to))
Object.entries(BLOCKS).forEach(([from, to]) => alias(BuiltInRegistries.BLOCK, from, to))

console.info('Antarchy rename aliases registered')
