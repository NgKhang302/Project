import { useState } from "react";
import ListenButton from "./ListenButton";
import DialogueTranscript from "./DialogueTranscript";

export default function ListeningContent({ lesson }) {
    const [activeIndex, setActiveIndex] = useState(-1);
    const hasLines = lesson.dialogueLines?.length > 0;

    return (
        <div className="listening-tools">
            {lesson.audioUrl && (
                <audio controls className="lesson-audio" src={lesson.audioUrl}>
                    Your browser does not support the audio element.
                </audio>
            )}
            <ListenButton
                text={lesson.content}
                lines={hasLines ? lesson.dialogueLines : undefined}
                onLineStart={setActiveIndex}
                label={lesson.audioUrl ? "🔊 Nghe lại bằng giọng đọc máy" : "🔊 Nghe bằng giọng đọc máy"}
            />
            {hasLines && <DialogueTranscript lines={lesson.dialogueLines} activeIndex={activeIndex} />}
        </div>
    );
}
