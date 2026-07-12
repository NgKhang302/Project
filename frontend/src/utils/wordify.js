const WORD_PATTERN = /[A-Za-z']+/g;

export function wordifyHtml(html) {
    if (!html) return html || "";

    const doc = new DOMParser().parseFromString(html, "text/html");
    const walker = document.createTreeWalker(doc.body, NodeFilter.SHOW_TEXT);

    const textNodes = [];
    let node;
    while ((node = walker.nextNode())) {
        textNodes.push(node);
    }

    for (const textNode of textNodes) {
        const text = textNode.nodeValue;
        if (!WORD_PATTERN.test(text)) continue;
        WORD_PATTERN.lastIndex = 0;

        const fragment = doc.createDocumentFragment();
        let lastIndex = 0;
        let match;
        while ((match = WORD_PATTERN.exec(text))) {
            if (match.index > lastIndex) {
                fragment.appendChild(doc.createTextNode(text.slice(lastIndex, match.index)));
            }
            const span = doc.createElement("span");
            span.className = "word";
            span.dataset.word = match[0];
            span.textContent = match[0];
            fragment.appendChild(span);
            lastIndex = match.index + match[0].length;
        }
        if (lastIndex < text.length) {
            fragment.appendChild(doc.createTextNode(text.slice(lastIndex)));
        }

        textNode.parentNode.replaceChild(fragment, textNode);
    }

    return doc.body.innerHTML;
}
