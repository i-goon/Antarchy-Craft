ServerEvents.recipes(event => {
  event.remove({ id: 'explorerscompass:explorers_compass' })
  event.shaped('explorerscompass:explorerscompass', ['ABA', 'BCB', 'ABA'], {
    A: 'minecraft:popped_chorus_fruit',
    B: 'minecraft:shulker_shell',
    C: 'minecraft:recovery_compass'
  })
})
