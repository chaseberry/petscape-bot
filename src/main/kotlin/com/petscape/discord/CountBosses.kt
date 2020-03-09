package com.petscape.discord

import edu.csh.chase.sprint.Response
import edu.csh.chase.sprint.Sprint
import java.net.URLEncoder
import kotlin.system.exitProcess

fun main() {

    while (true) {
        print("ign: ")

        val ign = readLine()?.takeIf { it.isNotBlank() } ?: continue

        if (ign == "q") {
            break
        }

        val scores = getHighScores(ign) ?: continue

        print("Total level: ")
        println(scores[Index.overall].score)

        print("Petscape calced boss KC: ")
        println(petScapeBossKc(scores))


        val r = meetsPetscapeBasic(scores)
        println("$ign has $r Smiley reqs. They ${if (r >= 2) "meet" else "do not meet"} Petscapes Smiley reqs")
        println("Though, they might have the pet. This only checks boss, 99, total level.")
    }

    exitProcess(0)
}

fun petScapeBossKc(scores: Highscores): Int {
    val bosses = Index.values().filter { Tag.boss in it.tags } - arrayOf(Index.cox, Index.coxcm, Index.tob)

    return bosses.sumBy { scores.score(it) } + (scores.score(Index.cox) * 2) + (scores.score(Index.coxcm) * 4) + (scores.score(
        Index.tob
    ) * 4)
}

fun meetsPetscapeBasic(s: Highscores): Int {
    var reqs = 0

    if (s.score(Index.overall) >= 1500) {
        reqs += 1
    }

    if (s.byTag(Tag.boss) >= 2000) {
        reqs += 1
    }

    //Theres a better way, but im lazy
    val nonCombats = listOf(
        Index.cooking, Index.woodcutting, Index.fletching, Index.fishing, Index.firemaking, Index.crafting,
        Index.smithing, Index.mining, Index.herblore, Index.agility, Index.theiving, Index.slayer, Index.farming,
        Index.runecrafting, Index.hunter, Index.construction
    )

    if (nonCombats.any { s.score(it) == 99 }) {
        reqs += 1
    }

    return reqs
}

/*
m=
hiscore_oldschool_ironman
hiscore_oldschool_ultimate
hiscore_oldschool_hardcore_ironman
hiscore_oldschool_deadman
hiscore_oldschool_seasonal
hiscore_oldschool_tournament
 
 */

fun getHighScores(name: String): Highscores? {
    val url = "https://secure.runescape.com/m=hiscore_oldschool/a=924/index_lite.ws?player=${URLEncoder.encode(
        name,
        "UTF-8"
    )}"

    return when (val r = Sprint.get(url).get()) {
        is Response.Success -> Highscores(r.bodyAsString ?: return null)
        else -> null
    }
}

class Highscores(input: String) {

    private val hs = input.split("\n").filter { it.isNotBlank() }.map {
        Score(it.split(","))
    }

    operator fun get(index: Index) = hs[index.ordinal]

    fun score(index: Index) = get(index).score

    fun rank(index: Index): Int? = get(index).rank

    fun xp(index: Index): Int? = get(index).xp

    fun byTag(tag: Tag) = Index.values().filter { tag in it.tags }.sumBy { score(it) }

    fun dump(): String {
        return hs.mapIndexed { i, it ->
            val index = Index.values().getOrNull(i)?.name ?: "Unknown Index"

            "$index ::: $it"
        }.joinToString("\n")
    }
}

class Score(parts: List<String>) {
    val rank: Int? = parts[0].toInt().takeIf { it > -1 }
    val score: Int = parts[1].toInt().takeIf { it > -1 } ?: 0
    val xp: Int? = parts.getOrNull(2)?.toIntOrNull()

    override fun toString() = "rank=$rank, score=$score, xp=$xp"
}

enum class Index(vararg val tags: Tag) {
    overall,
    attack(Tag.skill),
    defence(Tag.skill),
    strength(Tag.skill),
    hitpoints(Tag.skill),
    ranged(Tag.skill),
    prayer(Tag.skill),
    magic(Tag.skill),
    cooking(Tag.skill),
    woodcutting(Tag.skill),
    fletching(Tag.skill),
    fishing(Tag.skill),
    firemaking(Tag.skill),
    crafting(Tag.skill),
    smithing(Tag.skill),
    mining(Tag.skill),
    herblore(Tag.skill),
    agility(Tag.skill),
    theiving(Tag.skill),
    slayer(Tag.skill),
    farming(Tag.skill),
    runecrafting(Tag.skill),
    hunter(Tag.skill),
    construction(Tag.skill),
    skip1, //leagues
    skip2, //lms
    skip3, //dmm
    allClues,
    beginners(Tag.clue),
    easys(Tag.clue),
    mediums(Tag.clue),
    hards(Tag.clue),
    elites(Tag.clue),
    masters(Tag.clue),
    skip4,
    sire(Tag.boss),
    hydra(Tag.boss),
    barrows(Tag.boss),
    bryophyta(Tag.boss),
    callisto(Tag.boss),
    cerb(Tag.boss),
    cox(Tag.boss, Tag.raids),
    coxcm(Tag.boss, Tag.raids),
    chaosEle(Tag.boss),
    chaosFanatic(Tag.boss),
    zily(Tag.boss),
    corp(Tag.boss),
    arch(Tag.boss),
    prime(Tag.boss),
    rex(Tag.boss),
    surpeme(Tag.boss),
    derangedArch(Tag.boss),
    graador(Tag.boss),
    mole(Tag.boss),
    gargs(Tag.boss),
    hespori(Tag.boss),
    kq(Tag.boss),
    kbd(Tag.boss),
    kraken(Tag.boss),
    kree(Tag.boss),
    kril(Tag.boss),
    mimic(Tag.boss),
    nightmare(Tag.boss),
    obor(Tag.boss),
    sarachnis(Tag.boss),
    scorpia(Tag.boss),
    skotizo(Tag.boss),
    guantlet(Tag.boss),
    hardGauntlet(Tag.boss),
    tob(Tag.boss, Tag.raids),
    thermy(Tag.boss),
    zuk(Tag.boss),
    jad(Tag.boss),
    vene(Tag.boss),
    vetion(Tag.boss),
    vork(Tag.boss),
    wintertodt(Tag.boss),
    zalcano(Tag.boss),
    zulrah(Tag.boss),
}

enum class Tag {
    skill, clue, boss, raids
}