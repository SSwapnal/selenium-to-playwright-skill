#!/usr/bin/env python3
"""Small LLM-assisted Selenium Java -> Playwright TypeScript migration runner."""
import argparse
import os
from pathlib import Path

from openai import OpenAI


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--input', required=True, help='Path to Selenium Java source file')
    parser.add_argument('--output', required=True, help='New TypeScript output path')
    parser.add_argument('--skill', default='../skills/selenium-to-playwright-migration/SKILL.md',
                        help='Path to migration SKILL.md (relative to this script working directory)')
    args = parser.parse_args()

    if not os.getenv('OPENAI_API_KEY'):
        raise SystemExit('OPENAI_API_KEY is missing. Set it in your shell; do not put it in source code.')

    source_path = Path(args.input).resolve()
    skill_path = Path(args.skill).resolve()
    output_path = Path(args.output).resolve()
    if not source_path.is_file():
        raise SystemExit(f'Input Java file not found: {source_path}')
    if not skill_path.is_file():
        raise SystemExit(f'Skill file not found: {skill_path}')
    if output_path.exists():
        raise SystemExit(f'Refusing to overwrite existing file: {output_path}')

    java_source = source_path.read_text(encoding='utf-8')
    skill = skill_path.read_text(encoding='utf-8')
    prompt = f'''Follow the migration skill below. Convert the supplied Selenium Java + TestNG test
into a complete Playwright Test TypeScript spec. Preserve the scenario and assertions.
Use existing project Page Objects if they are appropriate; otherwise keep the generated spec
self-contained. Output only the complete TypeScript source code, with no Markdown fences.

--- MIGRATION SKILL ---
{skill}

--- SELENIUM JAVA SOURCE ---
{java_source}
'''

    client = OpenAI()
    response = client.responses.create(
        model=os.getenv('OPENAI_MODEL', 'gpt-4.1-mini'),
        input=[{'role': 'user', 'content': [{'type': 'input_text', 'text': prompt}]}],
    )
    generated = response.output_text.strip()
    if generated.startswith('```'):
        generated = generated.split('\n', 1)[1]
        if generated.endswith('```'):
            generated = generated[:-3].strip()

    output_path.parent.mkdir(parents=True, exist_ok=True)
    output_path.write_text(generated + '\n', encoding='utf-8')
    print(f'Created: {output_path}')
    print('Next: review the generated code, then run it with npx playwright test <file>.')


if __name__ == '__main__':
    main()
