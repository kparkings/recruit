"use strict";
/*
 * SonarQube JavaScript Plugin
 * Copyright (C) 2011-2023 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
// https://sonarsource.github.io/rspec/#/rspec/S6544/javascript
Object.defineProperty(exports, "__esModule", { value: true });
exports.rule = void 0;
const eslint_plugin_1 = require("@typescript-eslint/eslint-plugin");
const core_1 = require("linting/eslint/rules/core");
const helpers_1 = require("../rules/decorators/helpers");
const decoration_1 = require("../linter/decoration");
/**
 * We keep a single occurence of issues raised by both rules, discarding the ones raised by 'no-async-promise-executor'
 * The current logic relies on the fact that the listener of 'no-misused-promises' runs first because
 * it is alphabetically "smaller", which is how we set them up in mergeRules.
 */
/**
 * start offsets of nodes that raised issues in typescript-eslint's no-misused-promises
 */
const flaggedNodeStarts = new Map();
const noFloatingPromisesRule = (0, decoration_1.sanitizeTypeScriptESLintRule)(eslint_plugin_1.rules['no-floating-promises']);
const decoratedNoFloatingPromisesRule = (0, helpers_1.interceptReport)(noFloatingPromisesRule, (context, descriptor) => {
    var _a;
    if ('node' in descriptor) {
        const equivalentNode = (_a = descriptor.node.expression.arguments) === null || _a === void 0 ? void 0 : _a[0];
        if (equivalentNode) {
            const start = equivalentNode.range[0];
            flaggedNodeStarts.set(start, true);
        }
    }
    context.report(descriptor);
});
const noMisusedPromisesRule = (0, decoration_1.sanitizeTypeScriptESLintRule)(eslint_plugin_1.rules['no-misused-promises']);
const decoratedNoMisusedPromisesRule = (0, helpers_1.interceptReport)(noMisusedPromisesRule, (context, descriptor) => {
    if ('node' in descriptor) {
        const start = descriptor.node.range[0];
        if (!flaggedNodeStarts.get(start)) {
            flaggedNodeStarts.set(start, true);
            context.report(descriptor);
        }
    }
});
const noAsyncPromiseExecutorRule = core_1.eslintRules['no-async-promise-executor'];
const decoratedNoAsyncPromiseExecutorRule = (0, helpers_1.interceptReport)(noAsyncPromiseExecutorRule, (context, descriptor) => {
    if ('node' in descriptor) {
        const start = descriptor.node.range[0];
        if (!flaggedNodeStarts.get(start)) {
            context.report(descriptor);
        }
    }
});
// we don't want to suggest to use the void operator
const noFloatingPromisesMessages = noFloatingPromisesRule.meta.messages;
noFloatingPromisesMessages.floatingVoid = noFloatingPromisesMessages.floating;
exports.rule = {
    meta: {
        messages: {
            ...decoratedNoMisusedPromisesRule.meta.messages,
            ...decoratedNoAsyncPromiseExecutorRule.meta.messages,
            ...noFloatingPromisesMessages,
        },
        hasSuggestions: true,
    },
    create(context) {
        return {
            'Program:exit': () => {
                flaggedNodeStarts.clear();
            },
            ...(0, helpers_1.mergeRules)(decoratedNoAsyncPromiseExecutorRule.create(context), decoratedNoMisusedPromisesRule.create(context), decoratedNoFloatingPromisesRule.create(context)),
        };
    },
};
//# sourceMappingURL=no-misused-promises.js.map