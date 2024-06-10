package org.jetbrains.storytale.runtime

class StoryBuilder {
  val list = mutableListOf<Story>()
  fun addStory(story: Story) = list.add(story)
}

fun buildStoryBook(builder: StoryBuilder.() -> Unit): List<Story> {
  return StoryBuilder().apply(builder).list
}
