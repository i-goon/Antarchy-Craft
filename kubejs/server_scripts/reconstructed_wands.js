ServerEvents.recipes(event => {
  event.shaped('reconstructedwands:stone_wand', ['  S', ' I ', 'I  '], {
    S: 'minecraft:stone', I: 'minecraft:stick'
  })
  event.shaped('reconstructedwands:iron_wand', ['  I', ' S ', 'S  '], {
    I: 'minecraft:iron_ingot', S: 'minecraft:stick'
  })
  event.shaped('reconstructedwands:diamond_wand', ['  D', ' S ', 'S  '], {
    D: 'minecraft:diamond', S: 'minecraft:stick'
  })
  event.shaped('reconstructedwands:netherite_wand', ['  N', ' S ', 'S  '], {
    N: 'minecraft:netherite_ingot', S: 'minecraft:stick'
  })
  event.shaped('reconstructedwands:infinity_wand', ['  N', ' S ', 'S  '], {
    N: 'minecraft:nether_star', S: 'minecraft:stick'
  })
  event.shaped('reconstructedwands:core_angel', [' #X', '#O#', 'X# '], {
    X: '#c:ingots/gold', O: '#c:feathers', '#': '#c:glass_panes'
  })
  event.shaped('reconstructedwands:core_destruction', [' #X', '#O#', 'X# '], {
    X: 'minecraft:diamond_pickaxe', O: '#c:storage_blocks/diamond', '#': '#c:glass_panes'
  })
})
