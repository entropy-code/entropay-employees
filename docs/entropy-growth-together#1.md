---
theme: gaia
_class: lead
paginate: true
backgroundColor: #fff
---

# Entropy
Grow Together

---

# Agenda
1. Introduction about myself
2. We Grow Together
3. Some questions to you
4. Vibe coding!?
5. Sorteo

---

# Introduction

## Esteban Azaretzky

- CTO & Co-founder at **Entropy**
- Leading tech and operations for a 70+ person LatAm-based staff augmentation firm
- Focused on building high-performing, scalable teams for U.S.-based companies
- Now full-time CTO — fully committed to Entropy’s growth

---

## A bit more personal

- Married, with two young daughters 💛
- Fell in love with programming during the first year of university
- Been coding ever since — Java is my main language
- Fan of the Spring Framework  
- Color-blind 👀 — not a fan of frontend (maybe related 😄)

---

## My work at Entropy
 
- Started as a part-time CTO while also working with clients
- Projects: **Dash**, **Vehlo**, and **Tracera**
- Always focused on client success and team performance

---

## Entroteam: my current project

- Even as a full-time CTO, I didn’t want to stop coding
- **Entroteam** became my main side-project inside Entropy
- It's the one repo always opens on my laptop
- This presentation is about how I developed a key feature in it 🚀
---

# We Grow Together 🌱

- The most successful companies don’t just scale teams — they **build communities**.
- Community fosters:
  - Belonging
  - Growth
  - Shared identity
  - Long-term engagement
- It turns individuals into **collaborators** and teams into **movements**.
- That’s how culture scales — and how purpose stays alive.

---

## Our community-driven growth model

- At Entropy, our MTP is: **Develop People, Elevate Teams**
- We want to live that purpose by creating spaces to:
  - Learn from each other
  - Share knowledge
  - Mentor and be mentored
  - Celebrate progress and effort
- **We Grow Together** is not just a value — it’s our strategy to build a living, breathing **community of Entropists**.
- This program, and this talk, are part of that commitment.

---

# A Space for Entropistas 💬🤝

## A community built by and for us

- **We Grow Together** is an invitation:
  - To share thoughts and technical learnings
  - To connect beyond daily projects
  - To discuss trending technologies
  - To explore different approaches to solving problems
- A space where we grow not just as professionals, but as a **network of peers** who support, challenge, and inspire each other.

---

# Some questions to you

---

# Let’s talk about AI 🤖

## I want to know how you're using AI tools

We’ll answer three quick multiple-choice questions via **Slido**:

1. **Are you currently using any AI tools regularly?**  
   
2. **How do you use AI tools in your workflow?**  
   
3. **Which best describes your behavior when searching for coding help?**  
   
👉 Go to **https://app.sli.do/event/aef4GtZ5k8rv81EacSkkkD**

---

# Turnover Report 🧠

## A 3-day journey building a critical business metric

- A new report to track **client/project/company turnover**
- Key to understanding our retention performance
- Built with AI tools + classic intuition
- A mix of automation, frustration... and breakthroughs

---

# Day 1 – Starting with intuition

- No full spec, just a rough idea: "I want a turnover report"
- First prompt: HQL-based implementation grouped by project/client
- Quickly realized: I forgot to group by **year-month**
- Initial output was **flat JSON** – not ideal for analysis
- Decided to go for a **3-level hierarchical structure**

---

# From flat to structured

- Used ChatGPT (o3) to refactor the JSON
- Defined company → client → project hierarchy
- Added both **overall** and **year-month** breakdowns at each level
- Wrote a detailed transformation spec with rules & edge cases
- Results were *acceptable*, but code still too long and repetitive

---

# Junie helps… until it doesn’t

- Asked Junie to focus solely on hierarchical version
- Code got cleaner, but still lacked tests
- Prompted it to create **TurnoverServiceTest** with >80% coverage
- Junie crashed mid-process 😩
- Restarted and managed to get something acceptable
- Until… **real data showed it was wrong**

---

# Root cause: hidden complexity

- Problem: assignment records change when billable rate changes
- Those changes shouldn't count as turnover
- Added a rule:
  - If assignment continues in same project, **don’t count as turnover**
  - Only if project or client changes (or exit), it counts
- Prompted Junie with that business logic

---

# When AI fails, SQL prevails

- Refactored the backend to **leverage SQL** instead of Java grouping
- Used `generate_series()` to expand by year-month
- Avoided double-counting and false positives
- Query returns clean, already-grouped data
- Now the service layer only builds the DTOs

---

# Now we’re talking…

- Clean DTO structure
- Code much shorter and easier to follow
- First **manual commit** — the contract looks solid
- Time to delegate again to Junie: implement the logic cleanly

---

# Day 2 – Testing like a human

- Bug found: bad data in Entroteam
  - `endDate` incorrectly set to `now()` on deactivation
- Fixed the bug, but didn't block myself:
  - Kept working on tests using known clean data
- Manually verified edge cases using **pen & paper**

---

# The bug vs. the mission 🐛

## What happened?

- Discovered a bug in production:
  - When deactivating employees, `endDate` of assignments/contracts was incorrectly set to `now()`
- This corrupted historical data, impacting the **accuracy of turnover metrics**
- But… the bug had been there **long before the report existed**

---

# A critical decision 🎯

## Pause and fix, or move forward?

- Option A: pause feature work, deep-dive into DB backups, fix all historical data
- Option B: accept current state, focus on delivering the new report
- I chose **B**, because:
  - Entroteam's invoicing and payroll reports still worked
  - The bug wasn’t affecting business operations
  - Validating the logic and structure of the report was a higher priority

--- 

# Why it matters 🧠

## Progress ≠ perfection

- Sometimes, being “stuck” trying to fix everything **blocks meaningful progress**
- I documented the issue and planned the data correction for later
- The focus stayed on:
  - Testing the logic manually
  - Making sure the **feature added value**
  - Avoiding scope creep and missing the goal

--- 

# Why it matters 🧠

## Progress ≠ perfection

👉 Strategic tradeoffs are part of building software.  
And **acknowledging imperfection** is often a sign of maturity.

--- 

# Why turnover matters 🧩

- Entroteam powers core processes:
  - Salaries
  - Billing
  - Margins
- Now: **Turnover**
  - Measures our success with clients
  - Links satisfaction + retention
  - Voluntary exits = key signal

---

# Day 3 – Finishing strong

- Wrote more test scenarios:
  - Employee moves from project to project
  - From one client to another
  - Still hired cases
- Refactored test suite using Sonnet 4 — cleaner, parametric tests

---

# Final steps

- Created flat version of the report for frontend
- Reused same logic, applied transformation
- Implemented pagination and `ReportDto<TurnoverEntryDto>`
- Tested thoroughly — logic now feels rock solid
- Manually handled UI in React Admin

---

# Key Takeaways ✨

- AI is a tool — not magic
- Specs matter more than prompts
- SQL can save you when logic gets messy
- Short, readable code wins in the long run
- Writing tests manually gives confidence that the AI can’t

---

# Final thought 💭

> **"Specs matter more than prompts..."**  
> ...but it's better to build than to overthink.

- When specs are unclear, progress comes from **trying**, not waiting.
- AI is incredibly useful to **prototype fast**, explore options, and find the shape of the problem.
- Thinking and doing can go in parallel — one unlocks the other.

🚀 Just start — iterate — improve
